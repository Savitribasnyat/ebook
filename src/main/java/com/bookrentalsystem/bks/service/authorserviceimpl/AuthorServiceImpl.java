package com.bookrentalsystem.bks.service.authorserviceimpl;

import com.bookrentalsystem.bks.dto.author.AuthorRequest;
import com.bookrentalsystem.bks.dto.author.AuthorResponse;
import com.bookrentalsystem.bks.exception.globalexception.AuthorBookNotFound;
import com.bookrentalsystem.bks.exception.globalexception.AuthorCanNotBeDeletedException;
import com.bookrentalsystem.bks.model.Author;
import com.bookrentalsystem.bks.repo.AuthorRepo;
import com.bookrentalsystem.bks.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService  {

    private final AuthorRepo authorRepo;
    //this method is used to add the author in our database
    public String addAuthorDb(AuthorRequest authorRequest){
        authorRequest.setName(authorRequest.getName().trim());
        authorRequest.setEmail(authorRequest.getEmail().trim());

        Author author = authorRequestToEntity(authorRequest);

        Optional<Author> dbAuthorEmailTrue = authorRepo.findByEmailAndDeletedIsTrue(authorRequest.getEmail());
        Optional<Author> dbAuthorNumberTrue = authorRepo.findByNumberAndDeletedIsTrue(authorRequest.getNumber());

        if(dbAuthorEmailTrue.isPresent()){
            Author inActiveAuthor = dbAuthorEmailTrue.get();
                inActiveAuthor.setName(author.getName());
                inActiveAuthor.setNumber(author.getNumber());
                inActiveAuthor.setEmail(author.getEmail());
                inActiveAuthor.setDeleted(Boolean.FALSE);
                authorRepo.save(inActiveAuthor);
                return "revive the author having same email";
            }

        if(dbAuthorNumberTrue.isPresent()){
            Author inActiveAuthor = dbAuthorNumberTrue.get();
                inActiveAuthor.setName(author.getName());
                inActiveAuthor.setNumber(author.getNumber());
                inActiveAuthor.setEmail(author.getEmail());
                inActiveAuthor.setDeleted(Boolean.FALSE);
                authorRepo.save(inActiveAuthor);
                return "revive the author having same phone number";
        }

         authorRepo.save(author);
        return "save the author in db";
    }



    @Override
    public String updateAuthorAdd(AuthorRequest authorRequest) {
        Author author = authorRequestToEntity(authorRequest);
        authorRepo.save(author);
        return "updated";
    }

    //this method take parameter as AuthorRequest & convert the request into Author entity
    public Author authorRequestToEntity(AuthorRequest authorRequest){
        Author author = new Author();
        author.setId(authorRequest.getId());
        author.setName(authorRequest.getName());
        author.setEmail(authorRequest.getEmail());
        author.setNumber(authorRequest.getNumber());
        return author;
    }

    /*
    * converting the Author entity to AuthorResponse
     */
    public AuthorResponse entityToAuthorResponse(Author author){
        return AuthorResponse.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .number(author.getNumber())
                .build();
    }

    /*
    * from the id finding the author
    * And collecting it to seprate Author list
    * returning the author list
     */
    public List<Author> convertToAuthorList(List<Long> ids){
        return ids.stream().map(this::findAuthorById).toList();
    }

    /*
    *converting the author list to AuthorResponse list
     */
    public List<AuthorResponse> convertToAuthorResponseList(List<Author> authors){
        return authors.stream()
                .map(this::entityToAuthorResponse).toList();
    }

    //this method is used to find all the author list and return it in AuthorResponse form
    @Override
    public List<AuthorResponse> allAuthor() {
        List<Author> allAuthors = authorRepo.findAll();
        return convertToAuthorResponseList(allAuthors);
    }

    @Override
    public List<Author> allAuthors() {
        return authorRepo.findAll();
    }

    //take parameter as Id & return Author
    public Author findAuthorById(Long id)  {
        Optional<Author> singleAuthor = authorRepo.findById(id);
        if(singleAuthor.isPresent()){
            return singleAuthor.get();
        }
        throw new AuthorCanNotBeDeletedException("Author having this does not exist!!!");
    }

    //take parameter as Id  & return AuthorResponse
    @Override
    public AuthorResponse findAuthorResponseById(Long id) {
       Optional<Author> singleAuthor =  authorRepo.findById(id);
       if(singleAuthor.isPresent()){
           Author author = singleAuthor.get();
           return entityToAuthorResponse(author);
       }
        throw new AuthorCanNotBeDeletedException("Author having this id does not exist!!!");
    }

    //take parameter as Id  & return AuthorResponse
    @Override
    public AuthorResponse findAuthorNameResponseById(Long id) {
        Optional<Author> singleAuthor =  authorRepo.findById(id);
        if(singleAuthor.isPresent()){
            Author author = singleAuthor.get();
            return entityToAuthorResponse(author);
        }
        throw new AuthorBookNotFound("Sorry, Author book is not available currently!!!");
    }

    //this method is used to delete author by its id
    @Override
    public String deleteAuthor(Long id) {
        authorRepo.deleteById(id);
        return "deleted successfully";
    }

}
