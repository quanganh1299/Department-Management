document.getElementById("reset-password-form").onsubmit = onResetPasswordFormSubmit;

async function onResetPasswordFormSubmit(e) {
  e.preventDefault();

  const token = document.getElementById("token");
  const password = document.getElementById("password");
  const passwordConfirm = document.getElementById("password-confirm");
  const formData = new FormData();
  formData.append("token", token.value);
  formData.append("password", password.value);

  if(password.value!=passwordConfirm.value){
    alert("Mật khẩu nhập vào chưa khớp nhau, đề nghị bạn nhập lại!");
    return false;
  }


  // Gọi API Spring Boot để đặt lại mật khẩu
  const response = await fetch("http://localhost:8080/api/v1/password/reset-password", {
    method: "POST",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams(formData),
  });
  
  const content = await response.text();
  if (response.ok) {
    alert("Mật khẩu của bạn đã được cập nhật thành công!");
    window.open("./login.html", "_self");
  } else {
    alert("Lỗi: " + content);
  }
}





