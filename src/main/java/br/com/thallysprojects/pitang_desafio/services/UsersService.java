package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.MeDTO;
import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
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

    public MeDTO getLoggerDTO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        Users user = repository.findByLogin(login);

        return mapper.toMeDTO(user);
    }

}