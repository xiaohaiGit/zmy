$(function () {
  $("#login-button").click(function () {
    var email = $("#email").val();
    var password = $("#password").val();

    $.ajax({
      method: 'POST',
      url: "/zmy/login",
      // contentType: 'application/json',
      data: {
        email: email,
        password: password
      },
      success: function (resp) {
        if (resp.code == 200) {
          localStorage.setItem("token", resp.data);
          window.location.href = "/index.html"
        } else {
          alert(resp.desc);
        }
      }
    });

  });
});
