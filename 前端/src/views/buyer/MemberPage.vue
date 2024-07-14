<template>
  <br>
  <div class="container-fluid p-4" style="background-color: #FFFFAA; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
    <div class="merchant-wrapper"
      style="max-width: 800px; margin: auto; padding: 10px; background-color: #ffffff; border-radius: 8px; transition: box-shadow 0.3s ease;">

      <div class="header">
        <div class="row align-items-center justify-content-center text-center">
          <div class="col-12 col-md-6 d-flex align-items-center justify-content-center">
            <img class="logo rounded-circle me-3" :src="`${API_URL}/member/photo/${id}`" alt="商家圖片"
              style="width: 100px; height: 100px;" loading="lazy">
            <div class="d-flex flex-column align-items-center">
              <h2 class="mb-0 mt-2" style="font-family: 'Comic Sans MS'">{{ memberName }}</h2>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>


  <div class="row row-cols-1 row-cols-sm-2 row-cols-md-5 g-3 text-center mt-2">
    <div class="col" v-for="p in products" :key="p.productId">
      <div class="card border-width h-100 shadow">
        <div>
          <router-link :to="{ path: '/product/' + p.productId }">
            <img :src="`${API_URL}/product/photo/${p.productId}`" class="w-100" width="200px" height="200px" />
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
  </div>
  <br />
  <div class="row">
    <div class="col">
      <ul class="pagination justify-content-center">
        <li class="page-item">
          <button class="page-link" @click="minusPage()">
            <span>&laquo;</span>
          </button>
        </li>
        <li class="page-item" v-for="p of showPageBar" @click="goToPage(p)" :class="{ active: p == currentPage }">
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
</template>

<script>
import axios from "axios";
import Member from "../manager/Member.vue";


export default {
  data() {
    return {
      id: "",
      memberName: "",
      currentPage: 0,
      totalPage: 0,
      products: [],
    };
  },
  mounted() {
    this.id = this.$route.params.id;
    if (this.id) {
      this.searchProduct();
      this.fetchMemberName();
    }
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
  },
  methods: {
    searchProduct() {
      axios
        .get(`${this.API_URL}/member/product/${this.id}/${this.currentPage}`)
        .then((rs) => {
          this.currentPage = 0;
          this.products = rs.data;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        });
    },
    goToPage(p) {
      if (p == "...") {
        return;
      }
      this.currentPage = p;
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
    fetchMemberName() {
      axios.get(`${this.API_URL}/member/${this.id}`).then((rs) => {
        this.memberName = rs.data.memberName
        console.log(this.memberName)
      })
    }
  },
  watch: {
    "$route.params.id": function (newId, oldId) {
      if (newId !== oldId) {
        this.id = newId;
        this.searchProduct();
      }
    },
    currentPage(newValue, oldValue) {
      if (newValue !== oldValue) {
        axios
          .get(`${this.API_URL}/member/product/${this.id}/${this.currentPage}`)
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
}
</style>
