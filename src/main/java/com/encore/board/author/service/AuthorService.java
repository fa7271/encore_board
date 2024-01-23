package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.Author.AuthorDetailResDto;
import com.encore.board.author.dto.Author.AuthorListResDto;
import com.encore.board.author.dto.Author.AuthorSaveReqDto;
import com.encore.board.author.dto.Author.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) {
        Role role = null;
        if (authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")) {
            role =Role.USER;
        }
        else {
            role = Role.ADMIN;
        }
//        일반 생성자 방식
//        Author author = new Author(
//                authorSaveReqDto.getName(),
//                authorSaveReqDto.getEmail(),
//                authorSaveReqDto.getPassword(),
//                role
//        );
        Author author = Author.builder()
                .name(authorSaveReqDto.getName())
                .email(authorSaveReqDto.getEmail())
                .password(authorSaveReqDto.getPassword())
                .build();
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
        String role = null;
        if (author.getRole() == null || author.getRole().equals(Role.USER)) {
            role = "일반유저";
        }
        else {
            role = "관리자";
        }

        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        authorDetailResDto.setRole(role);
        return authorDetailResDto;
    }

    public void update(long id, AuthorUpdateReqDto authorUpdateReqDto) throws EntityNotFoundException{
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없음"));
        author.updateAuthor(authorUpdateReqDto.getName(),authorUpdateReqDto.getPassword());
        authorRepository.save(author);

//        AuthorDetailResDto author = this.findByID(id);
//        author.updateAuthor(authorUpdateReqDto.getName(),authorUpdateReqDto.getPassword());
//        authorRepository.save(author);
    }

    public void delete(long id) throws EntityNotFoundException{
        authorRepository.deleteById(id);

//        authorRepository.delete(authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없음")));
    }
}


//findbyid 메소드 효율 올리기

   /* public AuthorDetailResDto findByIDDetail(long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없음"));
        String role = null;
        if (author.getRole() == null || author.getRole().equals(Role.USER)) {
            role = "일반유저";
        }
        else {
            role = "관리자";
        }

        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        authorDetailResDto.setRole(role);
        return authorDetailResDto;
    }*/
  /* public Author findByID(long id) throws EntityNotFoundException {
       Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("없음"));

       return author;
   }*/

