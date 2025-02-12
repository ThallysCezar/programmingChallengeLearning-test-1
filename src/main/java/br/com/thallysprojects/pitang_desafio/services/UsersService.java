package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersGeneralException;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
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

    public List<UsersDTO> findAll() {
        List<Users> users = repository.findAll();
        if (users.isEmpty()) {
            HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;
            throw new UsersNotFoundException("Nenhum usuário encontrado", httpStatusCode.value());
        }
        return mapper.toListDTO(users);
    }

    public UsersDTO findById(Long id) {
        return repository.findById(id).map(mapper::toDTO).orElseThrow(UsersNotFoundException::new);
    }

    //Fazer para pageable
//    public UsersDTO findAllWithPageable(Pageable pageable) throws Exception {
//        return repository.findAllWithPageable(pageable)
//                .map(mapper::toDTO)
//                .orElseThrow(Exception::new);
//    }

    public void updateUserById(Long id) {
        try {
            HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;
            Users existingUser = repository.findById(id).orElseThrow(() -> new UsersNotFoundException(String.format("Usuário não encontrado com o id '%s'.", id), httpStatusCode.value()));
            mapper.toDTO(repository.saveAndFlush(existingUser));

        } catch (Exception e) {
            HttpStatus httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;

            log.error("Erro desconhecido ao atualizar o usuário: {}", e.getMessage(), e);
            throw new UsersGeneralException("Erro desconhecido ao atualizar o usuário: {}", httpStatusCode.value());
        }
    }

    public void save(UsersDTO dto) {
        try {
            mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception ex) {
            HttpStatus httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;

            log.error("Erro desconhecido ao salvar um usuário: {}", ex.getMessage(), ex);
            throw new UsersGeneralException("Erro desconhecido ao salvar um usuário", httpStatusCode.value());
        }
    }

    public void deleteUsers(Long id) {
        if (!repository.existsById(id)) {
            HttpStatus httpStatusCode = HttpStatus.NOT_FOUND;

            throw new UsersNotFoundException(String.format("Usuário não encontrado para o ID '%s'.", id), httpStatusCode.value());
        }
        repository.deleteById(id);
    }

}