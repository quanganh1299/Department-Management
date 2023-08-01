package com.vti.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vti.controller.AccountController;
import com.vti.controller.DepartmentController;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Getter
@Setter
public class DepartmentDto extends RepresentationModel<DepartmentDto> {
    private Integer id;
    private String name;
    private Integer totalMembers;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<AccountDto> accounts;

    public DepartmentDto withSelfRel(){
        if(accounts != null){
            for(AccountDto account : accounts){
                Link link = linkTo(methodOn(AccountController.class).findById(account.id)).withSelfRel();
                account.add(link);
            }
        }
        Link link = linkTo(methodOn(DepartmentController.class).findById(id)).withSelfRel();
        return add(link);
    }

    @Getter
    @Setter
    public static class AccountDto extends RepresentationModel<AccountDto> {
        //AccountDto này là AccountDto trong DepartmentDto
        @JsonProperty("accountId") //đổi tên của thuộc tính khi hiển thị
        private Integer id;
        private String username;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
