package com.vti.form;

import com.vti.entity.Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
// Tạo class form để đặt những thành phần cần lọc dữ liệu tại đây
public class AccountFilterForm {
    private String search;
    private Integer minId;
    private Integer maxId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //định dạng ngày
    private LocalDate minCreatedDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) //định dạng ngày
    private LocalDate maxCreatedDate;
    private Account.Role role;
}
