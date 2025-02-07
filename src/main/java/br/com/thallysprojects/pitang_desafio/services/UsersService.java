package br.com.thallysprojects.pitang_desafio.services;

import br.com.thallysprojects.pitang_desafio.dtos.UsersDTO;
import br.com.thallysprojects.pitang_desafio.entities.Users;
import br.com.thallysprojects.pitang_desafio.mappers.UsersMapper;
import br.com.thallysprojects.pitang_desafio.repositories.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsersService {

    private final UsersRepository repository;

    private final UsersMapper mapper;

    public List<UsersDTO> findAll() throws Exception {
        List<Users> users = repository.findAll();
        if(users.isEmpty()){
            throw new Exception("Nenhum usuário encontrado");
        }
        return mapper.toListDTO(users);
    }


    public UsersDTO findById(Long id) throws Exception {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(Exception::new);
    }

    public UsersDTO updateUserById(Long id){
        try{
            Users existingUser = repository.findById(id).orElseThrow(() -> new Exception("Usuário não com encontrando com o ID: " + id));
            return mapper.toDTO(repository.saveAndFlush(existingUser));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UsersDTO save(UsersDTO dto) throws Exception {
        try{
            return mapper.toDTO(repository.save(mapper.toEntity(dto)));
        } catch (Exception ex){
            throw new Exception();
        }
    }

    public void deleteUsers(Long id) throws Exception {
        if(!repository.existsById(id)){
            throw new Exception(String.format("Usuário não encontrado com o id '%s'.", id));
        }
        repository.deleteById(id);
    }

}
