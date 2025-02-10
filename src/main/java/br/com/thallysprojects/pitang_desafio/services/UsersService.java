package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import br.com.thallysprojects.pitang_desafio.exceptions.UsersNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository repository;

    private final UsersMapper mapper;

    public List<UsersDTO> findAll() {
        List<Users> users = repository.findAll();
        if (users.isEmpty()) {
            throw new UsersNotFoundException("Nenhum usuário encontrado");
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

    public UsersDTO updateUserById(Long id) {
        try {
            Users existingUser = repository.findById(id).orElseThrow(() -> new UsersNotFoundException(String.format("Usuário não encontrado com o id '%s'.", id)));
            return mapper.toDTO(repository.saveAndFlush(existingUser));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UsersDTO save(UsersDTO dto) throws Exception {
        try {
            return mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    public void deleteUsers(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new UsersNotFoundException(String.format("Usuário não encontrado com o id '%s'.", id));
        }
        repository.deleteById(id);
    }

}
