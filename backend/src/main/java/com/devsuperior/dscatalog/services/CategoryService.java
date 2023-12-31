package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = repository.findAll(pageRequest);

        //pode-se fazer um for para percorrer a lista que era de category e
        // agora será uma lista de categoryDTO:

        // List <CategoryDTO> listDto = new ArrayList<>();
        // for (Category cat : list);
        //listDTO add.Category cat = (new CategoryDTO (cat));
        //return listDto;

//Stream() é converter a coleção(no caso um list), é um recruso do java que permite trabalhar
// com funções de alta ordem, inclusive com expressões lambda,
// permitindo fazer transformações na coleção
// map - transforma algo original em outra coisa, aplicando uma função a cada elemento da lista.
//Para ficar masi rezumido pode-se usar a expressão Lambda:
// Para cada elemento "X" eu vou levál-o para outro elemento. Qual elemento?
// Será um new CatecoryDTO (x) ou seja,
// pega-se o list original e aplica-se a função lambda:map (x -> new CategoryDTO(x)) aqui o resultado
// será um stream e será necessário ocnertê-lo novamente em um List fazendo assim: .collect(Collectors.toList());
        return list.map(x -> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO upDate(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getOne(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (javax.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException(" Id not found" + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException(" Id not found" + id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DataBaseException("Integrity Violation");
        }
    }

}