<template>
  <main>
    <div class="d-flex flex-column">
      <div class="container" style="height: 700px">
        <div
          class="row row-cols-1 row-cols-sm-2 row-cols-md-5 g-3 text-center mt-4"
        >
          <template v-if="productOrSeller == 1">
            <div class="col" v-for="p in products" :key="p.productId">
              <div class="card border-width h-100 shadow">
                <div>
                  <router-link :to="{ path: '/product/' + p.productId }">
                    <img
                      :src="`${API_URL}/product/photo/${p.productId}`"
                      class="w-100"
                      width="200px"
                      height="200px"
                    />
                  </router-link>
                </div>

                <strong class="card-text mt-2 px-3 text-truncate">
                  {{ p.productName }}
                </strong>
                <div class="price fs-6 fw-bold text-primary mt-3">
                  <div class="m-3" v-if="p.minPrice != p.maxPrice">
                    NT $ {{ p.minPrice }} ~ ${{ p.maxPrice }}
                  </div>
                  <div class="m-3" v-if="p.minPrice == p.maxPrice">
                    NT $ {{ p.minPrice }}
                  </div>
                </div>
              </div>
            </div>
          </template>
          <!-- -------------------------------------上方為搜尋商品------------------- -->

          <template v-if="productOrSeller == 2">
            <div class="col" v-for="p in products" :key="p.productId">
              <div class="card border-width h-100 shadow">
                <div class="m-3">
                  <router-link :to="{ path: '/member/' + p.memberId }"
                    ><img
                      :src="`${this.API_URL}/member/photo/${p.memberId}`"
                      width="100%"
                  /></router-link>
                </div>
                <span class="member-id mt-2">{{ p.memberId }}</span>
                <strong>{{ p.memberName }}</strong>
              </div>
            </div>
          </template>
        </div>
        <!------------------------------------上方為搜尋賣家-------------------------->
      </div>
      <div class="row">
        <div class="col">
          <ul class="pagination justify-content-center">
            <li class="page-item">
              <button class="page-link" @click="minusPage()">
                <span>&laquo;</span>
              </button>
            </li>
            <li
              class="page-item"
              v-for="p of showPageBar"
              @click="goToPage(p)"
              :class="{ active: p == currentPage }"
            >
              <button class="page-link" v-if="p == '...'">{{ p }}</button>
              <button class="page-link" v-if="p != '...'">{{ p + 1 }}</button>
            </li>
            <li class="page-item">
              <button class="page-link" @click="plusPage()">
                <span>&raquo;</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
      <!-------------------------------------------------------------------------------------->
    </div>
  </main>
</template>
<script>
import axios from "axios";
import { useSearchStore } from "@/stores/searchStore";

export default {
  data() {
    return {
      currentPage: 0,
      totalPage: 0,
      products: [],
    };
  },
  mounted() {
    this.searchProduct();
  },
  computed: {
    showPageBar() {
      const cp = this.currentPage;
      const tp = this.totalPage;
      let arr = [0];
      if (cp > 4) {
        arr.push("...");
      }
      for (let i = cp - 2; i <= cp + 2; i++) {
        if (i > 0 && i < tp - 1) {
          arr.push(i);
        }
      }
      if (cp < tp - 3) {
        arr.push("...");
      }
      arr.push(tp - 1);
      return arr;
    },
    searchText() {
      const searchStore = useSearchStore();
      return searchStore.searchText;
    },
    productOrSeller() {
      const searchStore = useSearchStore();
      return searchStore.productOrSeller;
    },
  },
  methods: {
    goToPage(p) {
      if (p == "...") {
        return;
      }
      this.currentPage = p;
    },
    searchProduct() {
      if (this.searchText != "") {
        axios
          .get(
            `${this.API_URL}/searchProduct/${this.productOrSeller}/${this.searchText}/${this.currentPage}`
          )
          .then((rs) => {
            this.currentPage = 0;
            this.products = rs.data;
            if (rs.data != "") {
              this.totalPage = rs.data[0].totalPage;
            } else {
              this.totalPage = 1;
            }
          });
      }
    },
    minusPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
      }
    },
    plusPage() {
      if (this.currentPage < this.totalPage - 1) {
        this.currentPage++;
      }
    },
  },

  watch: {
    searchText(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.searchProduct();
      }
    },
    productOrSeller(newValue, oldValue) {
      if (newValue !== oldValue) {
        this.searchProduct();
      }
    },
    currentPage(newValue, oldValue) {
      if (newValue !== oldValue) {
        axios
          .get(
            `${this.API_URL}/searchProduct/${this.productOrSeller}/${this.searchText}/${this.currentPage}`
          )
          .then((rs) => {
            this.products = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>
<style scoped>
.card {
  border-radius: 1rem;
  transition-duration: 0.3s;
  background-color: #fefbb6fe;
  overflow: hidden;

  &:hover {
    transform: translateY(-6px);
    box-shadow: 0 4px 20px rgba(58, 58, 56, 0.35);
    background-color: #f9dd89;
  }

  .member-id {
    display: inline-block;
    width: 30px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    border-radius: 50%;
    background-color: #004b97;
    color: #ffffff;
    margin: 0 auto;
  }
}
</style>
