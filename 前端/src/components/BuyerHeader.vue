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
          <div class="d-flex align-items-center">
            <input
              class="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
              v-model="searchText"
            />
            <div style="width: 250px" class="me-2">
              <select
                class="form-select"
                aria-label="Default select example"
                v-model="productOrSeller"
              >
                <option value="1">搜尋商品</option>
                <option value="2">搜尋賣家</option>
              </select>
            </div>
            <button
              class="btn btn-outline-light"
              type="submit"
              @click="sendSearch"
            >
              <i class="bi-search"></i>
            </button>
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
              <button
                type="button"
                class="btn btn-outline-light"
                style="position: relative"
                @click="goToCart()"
              >
                <i class="bi bi-cart"></i>
                <span
                  class="badge rounded-pill bg-light text-dark"
                  style="position: absolute; top: 0; right: 0; font-size: 60%"
                ></span>
              </button>
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
import { useSearchStore } from "@/stores/searchStore";
import axios from "axios";

export default {
  data() {
    return {
      searchText: "",
      productOrSeller: "1",
      results: [],
    };
  },
  methods: {
    check() {
      axios.get(`${this.API_URL}/check`).then((rs) => console.log(rs.data));
    },
    logout() {
      axios
        .get(`${this.API_URL}/logout`)
        .then((rs) => {
          const memberStore = useMemberStore();
          memberStore.logout();
          sessionStorage.removeItem("loggedInMember");
          this.$router.push("/");
        })
        .catch((error) => {
          console.log("登出失敗", error);
        });
    },
    sendSearch() {
      if (this.searchText != "") {
        const searchText = this.searchText;
        const productOrSeller = this.productOrSeller;
        axios
          .get(`${this.API_URL}/searchProduct/${productOrSeller}/${searchText}`)
          .then((rs) => {
            const searchStore = useSearchStore();
            searchStore.search(searchText, productOrSeller);
            this.results = rs.data;
            this.searchText = "";
          })
          .catch((error) => {
            console.error(error);
          });
      } else {
        const searchText = "all";
        const productOrSeller = this.productOrSeller;
        const searchStore = useSearchStore();
        searchStore.search(searchText, productOrSeller);
        console.log("搜尋欄無文字輸入");
      }
      this.$router.push("/search");
    },
    goToCart() {
      this.$router.push("/cart");
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
    isLoggedIn() {
      return useMemberStore().isLoggedIn;
    },
  },
};
</script>

<style scoped>
.buyer-header {
  height: 10%;
}
</style>
