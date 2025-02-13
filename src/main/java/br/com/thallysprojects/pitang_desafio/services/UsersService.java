package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.utils.ValidationsUsers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersService {

    private final UsersRepository repository;

    private final UsersMapper mapper;

    private final ValidationsUsers validationsUsers;

    public List<UsersDTO> findAll() {
        List<Users> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UsersNotFoundException("Nenhum usuário encontrado", HttpStatus.NOT_FOUND.value());
        }
        return mapper.toListDTO(users);
    }

    public UsersDTO findById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(UsersNotFoundException::new);
    }

    public void updateUserById(Long id, UsersDTO dto) {
        try {
            Users existingUser = validationsUsers.validateUserExists(id);

            existingUser.setFirstName(dto.getFirstName());
            existingUser.setLastName(dto.getLastName());

            validationsUsers.validateEmailChange(existingUser, dto.getEmail());

            validationsUsers.validateLoginChange(existingUser, dto.getLogin());

            existingUser.setBirthday(dto.getBirthday());
            existingUser.setPhone(dto.getPhone());

            if (validationsUsers.isValidPassword(dto.getPassword())) {
                existingUser.setPassword(dto.getPassword());
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

    public void save(UsersDTO dto) {
        try {
            mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception ex) {
            log.error("Erro desconhecido ao salvar um usuário: {}", ex.getMessage(), ex);
            throw new UsersGeneralException("Erro desconhecido ao salvar um usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void deleteUsers(Long id) {
        if (!repository.existsById(id)) {
            throw new UsersNotFoundException(String.format("Usuário não encontrado para o ID '%s'.", id), HttpStatus.NOT_FOUND.value());
        }
        repository.deleteById(id);
    }

}