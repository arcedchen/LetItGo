<template>
  <main class="container col-xl-10 col-xxl-8 px-4 py-5">
    <div class="row align-items-center g-lg-5 py-5">
      <div class="col-md-10 mx-auto col-lg-5">
        <form class="p-4 p-md-5 border rounded-3 bg-light">
          <div class="form-title text-center fs-3 mb-3">註冊</div>
          <div class="form-floating mb-3">
            <input
              type="text"
              class="form-control"
              placeholder="name"
              v-model="name"
            />
            <label>name</label>
          </div>
          <div class="form-floating mb-3">
            <input
              type="text"
              class="form-control"
              placeholder="name@example.com"
              v-model="email"
            />
            <label>Email address</label>
          </div>
          <div class="form-floating mb-3">
            <input
              type="password"
              class="form-control"
              placeholder="Password"
              v-model="password"
            />
            <label>Password</label>
          </div>

          <button
            class="w-100 btn btn-lg btn-primary"
            @click.prevent="register"
          >
            註冊
          </button>
          <div class="text-danger text-center mt-3">{{ message }}</div>
          <hr class="my-4" />
          <small class="text-muted"
            >已經擁有LetIt購會員，馬上<a href="/login">登入</a>＾ㄧ＾</small
          >
        </form>
      </div>
    </div>
  </main>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      name: "",
      email: "@mail.com",
      password: "",
      message: "",
    };
  },
  methods: {
    register() {
      const fd = new FormData();
      fd.append("name", this.name);
      fd.append("email", this.email);
      fd.append("password", this.password);

      axios
        .post(`${this.API_URL}/register`, fd)
        .then((rs) => {
          this.$router.push("/login");
        })
        .catch(() => {
          this.message = "註冊失敗";
        });
    },
    check() {
      axios.get(`${this.API_URL}/check`).then((rs) => console.log(rs.data));
    },
  },
};
</script>
<style></style>
