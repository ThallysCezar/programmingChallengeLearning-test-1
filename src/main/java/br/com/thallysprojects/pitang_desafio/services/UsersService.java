package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersBadRequestException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.users.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsUsers;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Transactional
    public void save(@Valid UsersDTO dto) {
        try {
            validationsUsers.validateEmailExists(dto.getEmail(), repository);
            validationsUsers.validateLoginExists(dto.getLogin(), repository);
            validationsUsers.validateEmailFormat(dto.getEmail());
            validationsUsers.validateLoginFormat(dto.getLogin());
            validationsUsers.validatePasswordFormat(dto.getPassword());
            validationsUsers.validateMissingFields(dto);

            dto.setPassword(new BCryptPasswordEncoder().encode(dto.getPassword()));
            mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (UsersBadRequestException e) {
            throw new UsersGeneralException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
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
        Users user = repository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado com o email: " + username);
        }
        return user;
    }

    public MeDTO getLoggerDTO() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication.getName() == null) {
                throw new UsersNotFoundException("Nenhum usuário autenticado encontrado.", HttpStatus.UNAUTHORIZED.value());
            }

            String login = authentication.getName();

            Users user = repository.findByLogin(login);
            if (user == null) {
                throw new UsersNotFoundException("Usuário não encontrado para o login: " + login, HttpStatus.NOT_FOUND.value());
            }

            return mapper.toMeDTO(user);

        } catch (UsersNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro desconhecido ao obter dados do usuário logado: {}", e.getMessage(), e);
            throw new UsersGeneralException("Erro ao obter dados do usuário logado.", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}