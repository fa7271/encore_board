package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) {
        Author author = new Author(
                authorSaveReqDto.getName(),
                authorSaveReqDto.getEmail(),
                authorSaveReqDto.getPassword()

        );
        authorRepository.save(author);
    }

    public List<AuthorListResDto> findAll() {
        List<Author> list = authorRepository.findAll();
        List<AuthorListResDto> authorListResDtos = new ArrayList<>();
        for (Author m : list) {

            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(m.getId());
            authorListResDto.setName(m.getName());
            authorListResDto.setEmail(m.getEmail());

            authorListResDtos.add(authorListResDto);
        }
        return authorListResDtos;
    }

    public AuthorDetailResDto findByID(long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없음"));
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());

        return authorDetailResDto;

    }
}


