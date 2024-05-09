<template>
  <main class="container col-xl-10 col-xxl-8 px-4 py-5">
    <div class="row align-items-center g-lg-5 py-5">
      <div class="col-md-10 mx-auto col-lg-5">
        <div class="p-4 p-md-5 border rounded-3 bg-light">
          <div class="form-title text-center fs-3 mb-3">登入</div>
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
          <button class="w-100 btn btn-lg btn-primary" @click.prevent="login">
            登入</button
          ><button
            @click="gotoGoogleLoginPage"
            class="w-100 btn btn-lg btn-success mt-2"
          >
            Google 登入
          </button>
          <div class="text-danger text-center mt-3">{{ message }}</div>
          <hr class="my-4" />
          <small class="text-muted"
            >還不是LetIt購會員嗎？馬上<a href="/register">註冊</a>＾ㄧ＾</small
          >
        </div>
      </div>

      <!-- <div class="col-md-10 mx-auto col-lg-5">
        <form class="p-4 p-md-5 border rounded-3 bg-light">
          <div class="form-title text-center fs-3 mb-3">登入</div>
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
          <button class="w-100 btn btn-lg btn-primary" @click.prevent="login">
            登入
          </button>
          <div class="text-danger text-center mt-3">{{ message }}</div>
          <hr class="my-4" />
          <small class="text-muted"
            >還不是LetIt購會員嗎？馬上<a href="/register">註冊</a>＾ㄧ＾</small
          >
        </form>
        <button
          @click="gotoGoogleLoginPage"
          class="w-100 btn btn-lg btn-secondary mt-2"
        >
          Google 登入
        </button>
      </div> -->
    </div>
  </main>
</template>
<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      email: "bob@mail.com",
      password: "1234",
      message: "",
    };
  },
  methods: {
    login() {
      const fd = new FormData();
      fd.append("email", this.email);
      fd.append("password", this.password);

      axios
        .post(`${this.API_URL}/login`, fd)
        .then((rs) => {
          const memberStore = useMemberStore();
          memberStore.loginSuccess(rs.data);
          sessionStorage.setItem("loggedInMember", JSON.stringify(rs.data));
          this.$router.push("/");
        })
        .catch(() => {
          this.message = "登入失敗";
        });
    },
    check() {
      axios.get(`${this.API_URL}/check`).then((rs) => console.log(rs.data));
    },
    gotoGoogleLoginPage() {
      window.location.href = `${this.OA2_URL}/google-login`;
    },
  },
};
</script>
<style></style>
