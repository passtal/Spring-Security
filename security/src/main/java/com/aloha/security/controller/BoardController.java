package com.aloha.security.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aloha.security.dto.Board;
import com.aloha.security.dto.CustomUser;
import com.aloha.security.service.BoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;



    // 아래의 요청경로 매핑을 하기 위해서 컨트롤러 메소드를 작성해보세요
    // 게시글 목록  - [GET] /board      /board/list.html
    // 게시글 조회  - [GET] /board/{id}      /board/detail.html
    // 게시글 등록  - [GET] /board/create      /board/create.html
    // 게시글 등록 처리  - [POST] /board      /board/list.html
    // 게시글 수정  - [GET] /board/update/{id}      /board/update.html
    // 게시글 수정 처리  - [PUT] /board
    // 게시글 삭제 처리  - [DELETE] /board/{id}



    /**
     * 게시글 목록
     * [GET] /board
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("")
    public String boardList(Model model) throws Exception {
        log.info(":::::::::: 게시글 목록 조회 ::::::::::");
        List<Board> boardList = boardService.list();
        model.addAttribute("list", boardList);
        return "/board/list";
    }

    /**
     * 게시글 상세 조회
     * [GET] /board/detail.html
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable("id") String id, Model model) throws Exception {
        log.info(":::::::::: 게시글 상세 조회 ::::::::::");
        Board board = boardService.selectById(id);
        model.addAttribute("board", board);
        return "/board/detail";
    }

    /**
     * 게시글 등록 화면
     * [GET] /board/create.html
     * @Secured("ROLE_USER")
     * @return
     */
    // @PreAuthorize("hasRole('USER')")     // 유저 권한 체크
    @PreAuthorize("isAuthenticated()")      // 인증 체크
    @GetMapping("/create")
    public String boardCreate() {
        log.info(":::::::::: 게시글 등록 화면 ::::::::::");
        return "/board/create";
    }

    /**
     * 게시글 등록 처리
     * [POST] /board/create
     * @param board
     * @return
     * @throws Exception
     */
    // @PreAuthorize("hasRole('USER')")             // USER 권한 체크
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")    // USER or ADMIN 권한 체크
    @PostMapping("")
    public ResponseEntity<?> boardCreatePro(@AuthenticationPrincipal CustomUser customUser, @RequestBody Board board) throws Exception {
        
        log.info(":::::::::: 게시글 등록 처리 ::::::::::");

        try {
            // 현재 인증된 사용자 번호를(no) 등록요청한 게시글 데이터에 세팅
            Long userNo = customUser.getUser().getNo();
            board.setUserNo(userNo);

            boolean result = boardService.insert(board);
            if (!result) {
            return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST); // 400 error
            }
            return new ResponseEntity<>("Success", HttpStatus.CREATED); // 201 
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    /**
     * 게시글 수정 화면
     * [GET] /board/update.html
     * @param id
     * @param model
     * @return
     * @throws Exception
     *  @PreAuthorize
     * - 파라미터 값을 가져오는 방법
     *      : #p0, #p1 형태로 파라미터 인덱스를 지정하여 가져올 수 있다.
     *      여기서는 요청 파라미터로 넘어온 id 2번째에 있기 때문에
     *      인덱스로는 1번이 되어 #p1 이면 가져올 수 있음
     * - 서비스 메소드를 권한 제어 로직으로 활용하는 방법
     *      : "@빈이름" 형태로 특정 빈의 메소드를 호출할 수 있다.
     * * 여기서는 @BoardService.isOwner({id}, {userNo})
     */
    @PreAuthorize(" #p1 != null and @BoardService.isOwner( #p1, authentication.principal.user.no ) ")
    @GetMapping("/update/{id}")
    public String update (Model model, @PathVariable("id") String id) throws Exception {
        
        log.info(":::::::::: 게시글 수정 화면 ::::::::::");
        
        Board board = boardService.selectById(id);
        model.addAttribute("board", board);
        return "/board/update";
    }

    /**
     * 게시글 수정 처리
     * [PUT] /board/update
     * @param board
     * @return
     * @throws Exception
     */
    @PutMapping("")
    @PreAuthorize("#p0 != null and @BoardService.isOwner(#p0.id, authentication.principal.user.no)")
    public ResponseEntity<?> boardUpdatePro(@RequestBody Board board) throws Exception {
        log.info(":::::::::: 게시글 수정 처리 ::::::::::");
        try {
            boolean result = boardService.updateById(board);
            if (!result) {
                return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);       // 400
            }
            // [수정 3] 성공인데 "Fail"이라고 적혀 있어서 "Success"로 수정했습니다.
            return new ResponseEntity<>("Success", HttpStatus.OK);                 // 200
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }

    /**
     * 게시글 삭제 처리
     * [DELETE] /board/delete
     * @param no
     * @return
     * @throws Exception
     */
    // 관리자 . 작성자 검증
    @PreAuthorize("(hasRole('ADMIN')) or #p0 != null and @BoardService.isOwner( #p0, authentication.principal.user.no)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> boardDelete(@PathVariable("id") String id) throws Exception {

        log.info(":::::::::: 게시글 삭제 처리 ::::::::::");
        
        try {
            boolean result = boardService.deleteById(id);
            if (!result) {
                return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST); // 400
            }
            return new ResponseEntity<>("Success", HttpStatus.OK); // 200
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Fail", HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }
    }
}