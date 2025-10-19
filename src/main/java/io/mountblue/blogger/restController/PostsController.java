package io.mountblue.blogger.restController;

import io.mountblue.blogger.dto.PostDTO;
import io.mountblue.blogger.model.Post;
import io.mountblue.blogger.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    @Autowired
    PostService postService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<PostDTO> postsDTOpage = postService.getAllPostsAsDTO(pageable);
        Map<String, Object> filterData = postService.getFilterData();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postsDTOpage.getContent());
        response.put("currentPage", postsDTOpage.getNumber());
        response.put("totalItems", postsDTOpage.getTotalElements());
        response.put("totalPages", postsDTOpage.getTotalPages());
        response.put("tags", filterData.get("tags"));
        response.put("authors", filterData.get("authors"));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO postDTO = postService.getPostDTOById(id);
        return ResponseEntity.ok(postDTO);
    }

    @GetMapping("/filter")
    public ResponseEntity<Map<String, Object>> filterPost(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "") List<String> tags,
            @RequestParam(required = false, defaultValue = "") List<String> authors,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat LocalDateTime to,
            @RequestParam(defaultValue = "desc") String sortDirection,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<PostDTO> postDTOPage = postService.getFilteredPostsAsDTO(
                pageable, keyword, tags, authors, from, to);
        Map<String, Object> filterData = postService.getFilterData();

        Map<String, Object> response = new HashMap<>();
        response.put("posts", postDTOPage.getContent());
        response.put("currentPage", postDTOPage.getNumber());
        response.put("totalItems", postDTOPage.getTotalElements());
        response.put("totalPages", postDTOPage.getTotalPages());
        response.put("filters", Map.of(
                "keyword", keyword,
                "tags", tags,
                "authors", authors,
                "sortBy", sortBy,
                "sortDirection", sortDirection
        ));
        response.put("availableTags", filterData.get("tags"));
        response.put("availableAuthors", filterData.get("authors"));

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> savePost(
            @RequestBody Post post, Authentication authentication) {

        postService.createPost(post, authentication);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Post created successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updatePost(
            @PathVariable Long id,
            @RequestBody Post post,
            Authentication authentication) {

        postService.updatePostWithAuthorization(id, post, authentication);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Post updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id, Authentication authentication) {

        postService.deletePostWithAuthorization(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
