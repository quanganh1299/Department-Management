addListeners();

async function addListeners() {
    document.getElementById("login-form").onsubmit = onLoginFormSubmit;
    document.getElementById("forgot-password-form").onsubmit = onForgotPasswordFormSubmit;
}

async function onLoginFormSubmit(e) {
    e.preventDefault();
    const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        body: new FormData(this),
    });
    if(response.ok) {
        localStorage.setItem("bearer_token", response.headers.get("Authorization"));
        setTimeout(() => location.replace("../pages/accounts.html"), 1000);
    }
}

async function onForgotPasswordFormSubmit(e) {
  e.preventDefault();
  const email = document.getElementById("email");
  const formData = new FormData();
  formData.append("email", email.value);
  const response = await fetch("http://localhost:8080/api/v1/auth/forgot-password", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams(formData),
  });

  const content = await response.text();
  if (response.ok) {
      alert("Chúng tôi đã gửi hướng dẫn đặt lại mật khẩu đến email của bạn."); // Hiển thị thông báo thành công
      window.open("./reset-password.html", "_self");
  } else {
      alert("Lỗi: " + content); // Hiển thị thông báo lỗi
  }
}


const modal = document.getElementById('forgot-password-modal')
const btn = document.getElementById('forgot-password')
const closeBtn = document.querySelector('.close')

btn.onclick = () => {
  modal.style.display = 'block'
}

closeBtn.onclick = () => {
  modal.style.display = 'none'
}

window.onclick = (event) => {
  if (event.target === modal) {
    modal.style.display = 'none'
  }
}
