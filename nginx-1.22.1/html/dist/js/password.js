$(function () {

  var user = JSON.parse(localStorage.getItem("userinfo"));
  var email = user.email;
  $("#email").val(email);

  $.validator.setDefaults({
    submitHandler: function () {
      // alert("Form successful submitted!");

      var email = $("#email").val();
      var password = $("#password").val();
      var token = localStorage.getItem("token");
      if (token == undefined || token == null || token.length === 0) {
        window.parent.window.location.href = "/login.html";
      }

      $.ajax({
        method: 'POST',
        url: "/zmy/change-password",
        // contentType: 'application/json',
        headers: {
          token: token
        },
        data: {
          email: email,
          password: password
        },
        success: function (resp) {
          if (resp.code == 200) {
            // window.location.href = "/login.html"
            alert("change password success");
          } else if (resp.code == 302) {
            window.parent.window.location.href = "/login.html";
          } else {
            alert("change password failed , desc : " + resp.desc);
          }
        }
      });

    }
  });
  $('#quickForm').validate({
    rules: {
      email: {
        required: true,
        email: true,
      },
      password: {
        required: true,
        minlength: 6
      },
      terms: {
        required: true
      },
    },
    messages: {
      email: {
        required: "Please enter a email address",
        email: "Please enter a valid email address"
      },
      password: {
        required: "Please provide a password",
        minlength: "Your password must be at least 5 characters long"
      },
      terms: "Please accept our terms"
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
      error.addClass('invalid-feedback');
      element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
      $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
      $(element).removeClass('is-invalid');
    }
  });
});
