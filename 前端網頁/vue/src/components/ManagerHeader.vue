<template>
  <header class="buyer-header" style="background-color: #003060">
    <div class="container">
      <div class="row align-items-center py-3">
        <div class="col">
          <div class="text-start">
            <router-link to="/" class="text-decoration-none">
              <img
                src="/LetitgoLogo.jpg"
                width="35%"
                class="mx-2"
                style="border-radius: 10%"
              />
            </router-link>
          </div>
        </div>
        <div class="col-6">
          <div class="align-items-center text-white text-center fs-1">
            管理中心
          </div>
        </div>
        <div class="col">
          <div class="text-end">
            <router-link
              to="/login"
              class="text-decoration-none"
              v-if="!isLoggedIn"
            >
              <button type="button" class="btn btn-outline-light me-2">
                登入
              </button>
            </router-link>
            <template v-if="isLoggedIn">
              <img
                :src="`${API_URL}/member/photo/${id}`"
                width="17%"
                height="17%"
                class="mx-2"
                style="border-radius: 100%"
              />
              <button
                class="btn btn-outline-light dropdown-toggle"
                type="button"
                id="dropdownMenuButton1"
                data-bs-toggle="dropdown"
                aria-expanded="false"
              >
                {{ name }}
              </button>
              <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
                <li>
                  <router-link to="/profile" class="dropdown-item px-3"
                    >個人資料</router-link
                  >
                </li>
                <li>
                  <router-link to="/order" class="dropdown-item px-3"
                    >我的訂單</router-link
                  >
                </li>
                <li>
                  <router-link to="/wallet" class="dropdown-item px-3"
                    >我的錢包</router-link
                  >
                </li>
                <li>
                  <router-link to="/seller/products" class="dropdown-item px-3"
                    >賣家中心</router-link
                  >
                </li>
                <li>
                  <router-link to="/manager/member" class="dropdown-item px-3"
                    >管理中心</router-link
                  >
                </li>
                <li>
                  <hr class="dropdown-divider" />
                </li>
                <button
                  type="button"
                  class="dropdown-item nav-link px-3"
                  @click="logout"
                >
                  登出
                </button>
              </ul>
            </template>
          </div>
        </div>
      </div>
    </div>
  </header>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";
export default {
  methods: {
    logout() {
      axios
        .get(`${this.API_URL}/logout`)
        .then((rs) => {
          const memberStore = useMemberStore();
          memberStore.logout();
          this.$router.push("/");
        })
        .catch((error) => {
          console.log("登出失敗", error);
        });
    },
  },

  computed: {
    id() {
      const memberStore = useMemberStore();
      return memberStore.memberId;
    },
    name() {
      const memberStore = useMemberStore();
      return memberStore.memberName;
    },
    photo() {
      const memberStore = useMemberStore();
      return memberStore.memberPhoto;
    },
    isLoggedIn() {
      return useMemberStore().isLoggedIn;
    },
  },
};
</script>
<style>
.buyer-header {
  height: 10%;
}
</style>
