$(function () {
  $("#send-button").click(function () {
    var email = $("#email").val();

    $.ajax({
      method: 'POST',
      url: "/zmy/forgot-password",
      data: {
        email: email
      },
      success: function (resp) {
        if (resp.code == 200) {
          alert("send mail success , please check your email");
        } else {
          alert("send mail failed , please check your email address is exists or contact administrator ！desc : " + resp.desc);
        }
      }
    });

  });
});
