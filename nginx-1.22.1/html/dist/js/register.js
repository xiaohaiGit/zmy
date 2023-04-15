$(function () {
  $("#register-button").click(function () {
    var email = $("#email").val();
    var username = $("#username").val();
    var newPassword = $("#newPassword").val();
    var againPassword = $("#againPassword").val();

    if (newPassword != againPassword) {
      alert("两次输入的密码不一致")
      return;
    }

    $.ajax({
      method: 'POST',
      url: "/zmy/register",
      // contentType: 'application/json',
      data: {
        email: email,
        username: username,
        password: newPassword
      },
      success: function (resp) {
        if (resp.code == 200) {
          window.location.href = "/login.html"
        } else {
          alert("register failed , desc : " + resp.desc);
        }
      }
    });

  });
});
