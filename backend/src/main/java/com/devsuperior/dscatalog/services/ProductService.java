package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.exceptions.DataBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = repository.findAll(pageRequest);

        //pode-se fazer um for para percorrer a lista que era de Product e
        // agora será uma lista de ProductDTO:

        // List <ProductDTO> listDto = new ArrayList<>();
        // for (Product cat : list);
        //listDTO add.Product cat = (new ProductDTO (cat));
        //return listDto;

//Stream() é converter a coleção(no caso um list), é um recruso do java que permite trabalhar
// com funções de alta ordem, inclusive com expressões lambda,
// permitindo fazer transformações na coleção
// map - transforma algo original em outra coisa, aplicando uma função a cada elemento da lista.
//Para ficar masi rezumido pode-se usar a expressão Lambda:
// Para cada elemento "X" eu vou levál-o para outro elemento. Qual elemento?
// Será um new CatecoryDTO (x) ou seja,
// pega-se o list original e aplica-se a função lambda:map (x -> new ProductDTO(x)) aqui o resultado
// será um stream e será necessário ocnertê-lo novamente em um List fazendo assim: .collect(Collectors.toList());
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        //entity.setName(dto.getName());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO upDate(Long id, ProductDTO dto) {
        try {
            Product entity = repository.getOne(id);
           // entity.setName(dto.getName());
            entity = repository.save(entity);
            return new ProductDTO(entity);
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