package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.CarsGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsUsers;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService implements UserDetailsService {

    private final UsersRepository repository;

    private final UsersMapper mapper;

    private final ValidationsUsers validationsUsers;

    @Transactional
    public List<UsersDTO> findAll() {
        List<Users> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UsersNotFoundException("Nenhum usuário encontrado", HttpStatus.NOT_FOUND.value());
        }
        return mapper.toListDTO(users);
    }

    @Transactional
    public UsersDTO findById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(UsersNotFoundException::new);
    }

    @Transactional
    public void updateUserById(Long id, UsersDTO dto) {
        try {
            Users existingUser = validationsUsers.validateUserExists(id);

            existingUser.setFirstName(dto.getFirstName());
            existingUser.setLastName(dto.getLastName());

            validationsUsers.validateEmailChange(existingUser, dto.getEmail());
            validationsUsers.validateLoginChange(existingUser, dto.getLogin());

            existingUser.setBirthday(dto.getBirthday());
            existingUser.setPhone(dto.getPhone());
            existingUser.setRole(dto.getRole());

            if (validationsUsers.isValidPassword(dto.getPassword())) {
                String criptografarSenha = new BCryptPasswordEncoder().encode(dto.getPassword());
                existingUser.setPassword(criptografarSenha);
            }

            existingUser.setCars(validationsUsers.validateAndUpdateUserCars(existingUser, dto.getCars()));
            repository.saveAndFlush(existingUser);
        } catch (UsersNotFoundException | UsersGeneralException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro desconhecido ao atualizar o usuário: {}", e.getMessage(), e);
            throw new UsersGeneralException("Erro desconhecido ao atualizar o usuário.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

//    @Transactional
//    public void save(UsersDTO dto) {
//        try {
//            String criptografarSenha = new BCryptPasswordEncoder().encode(dto.getPassword());
//            dto.setPassword(criptografarSenha);
//            mapper.toDTO(repository.save(mapper.toEntity(dto)));
//        } catch (Exception ex) {
//            log.error("Erro desconhecido ao salvar um usuário: {}", ex.getMessage(), ex);
//            throw new UsersGeneralException("Erro desconhecido ao salvar um usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
//        }
//    }

    @Transactional
    public void save(@Valid UsersDTO dto) {
        try {
            //email - Email already exists
            if (repository.findByEmail(dto.getEmail()) != null) {
                throw new UsersNotFoundException("Email already exists", HttpStatus.BAD_REQUEST.value());
            }

            //login - Login already exists
            if (repository.findByLogin(dto.getLogin()) != null) {
                throw new UsersNotFoundException("Login already exists", HttpStatus.BAD_REQUEST.value());
            }

            //campos invalidos - Invalid fields
            // Validação do formato do e-mail - exemplo: exemplo@email.com
            if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new UsersNotFoundException("Invalid fields, email", HttpStatus.BAD_REQUEST.value());
            }

            // Validação do login (mínimo de 5 caracteres, sem espaços)
            // letras, números e underscores (_) e Não permite espaços
            if (!dto.getLogin().matches("^[a-zA-Z0-9_]{5,}$")) {
                throw new UsersNotFoundException("Invalid fields, login", HttpStatus.BAD_REQUEST.value());
            }

            // Validação da senha (mínimo 6 caracteres, ao menos 1 número e 1 letra)
            // Deve conter pelo menos 1 letra e 1 número
            if (!dto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")) {
                throw new UsersNotFoundException("Invalid fields, password", HttpStatus.BAD_REQUEST.value());
            }

            //campos nao preenchidos - Missing fields
            if (StringUtils.isBlank(dto.getEmail()) ||
                StringUtils.isBlank(dto.getLogin()) ||
                StringUtils.isBlank(dto.getPassword()) ||
                StringUtils.isBlank(dto.getFirstName()) ||
                StringUtils.isBlank(dto.getLastName())) {

                throw new UsersNotFoundException("Missing fields", HttpStatus.BAD_REQUEST.value());
            }

            String criptografarSenha = new BCryptPasswordEncoder().encode(dto.getPassword());
            dto.setPassword(criptografarSenha);
            mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (UsersGeneralException ex) {
            log.error("Erro desconhecido ao salvar um usuário: {}", ex.getMessage(), ex);
            throw new UsersGeneralException("Erro desconhecido ao salvar o carro", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Transactional
    public void deleteUsers(Long id) {
        if (!repository.existsById(id)) {
            throw new UsersNotFoundException(String.format("Usuário não encontrado para o ID '%s'.", id), HttpStatus.NOT_FOUND.value());
        }
        repository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username);
    }

//    @Transactional
//    public void criar(AuthenticationUserDTO dto) {
//        String criptografarSenha = new BCryptPasswordEncoder().encode(dto.getPassword());
//        Cliente cliente = new Cliente(dto.email(), criptografarSenha, dto.role(), dto.cpf(),  dto.apelido());
//
//        repository.save(cliente);
//    }

}