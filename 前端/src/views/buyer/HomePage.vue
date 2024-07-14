<template>
  <main>
    <div class="py-1">
      <div class="container">
        <div class="row mb-4" style="margin-top: 20px">
          <div
            id="carouselExampleIndicators"
            class="carousel slide"
            data-bs-ride="carousel"
            data-bs-interval="3000"
          >
            <div class="carousel-indicators">
              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="0"
                class="active"
                aria-current="true"
                aria-label="Slide 1"
              ></button>

              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="1"
                aria-label="Slide 2"
              ></button>

              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="2"
                aria-label="Slide 3"
              ></button>

              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="3"
                aria-label="Slide 4"
              ></button>

              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="4"
                aria-label="Slide 5"
              ></button>

              <button
                type="button"
                data-bs-target="#carouselExampleIndicators"
                data-bs-slide-to="5"
                aria-label="Slide 6"
              ></button>
            </div>
            <div class="carousel-inner">
              <div class="carousel-item active">
                <img src="/1.jpg" class="d-block w-100" />
              </div>
              <div class="carousel-item">
                <img src="/2.jpg" class="d-block w-100" />
              </div>
              <div class="carousel-item">
                <img src="/3.jpg" class="d-block w-100" />
              </div>
              <div class="carousel-item">
                <img src="/4.png" class="d-block w-100" />
              </div>
              <div class="carousel-item">
                <img src="/5.jpg" class="d-block w-100" />
              </div>
              <div class="carousel-item">
                <img src="/6.jpg" class="d-block w-100" />
              </div>
            </div>
            <button
              class="carousel-control-prev"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="prev"
            >
              <span
                class="carousel-control-prev-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Previous</span>
            </button>
            <button
              class="carousel-control-next"
              type="button"
              data-bs-target="#carouselExampleIndicators"
              data-bs-slide="next"
            >
              <span
                class="carousel-control-next-icon"
                aria-hidden="true"
              ></span>
              <span class="visually-hidden">Next</span>
            </button>
          </div>
        </div>
        <!-- 隨機產品----------------------------------------------------->

        <div
          class="row row-cols-1 row-cols-sm-2 row-cols-md-5 g-3 text-center mt-2"
          @mouseenter="stopUpdatingProducts"
          @mouseleave="resumeUpdatingProducts"
        >
          <div class="col" v-for="p in products" :key="p.productId">
            <div
              class="card border-width h-200 shadow"
              @mousemove="handleMouseMove($event, p.productId)"
              @mouseenter="handleMouseEnter($event, p.productId)"
              @mouseleave="handleMouseLeave($event, p.productId)"
              :ref="`card-${p.productId}`"
            >
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
                  NT$ {{ p.minPrice }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>
<script>
import axios from "axios";

export default {
  data() {
    return {
      products: [],
      updateInterval: null,
    };
  },
  beforeUnmount() {
    this.stopUpdatingProducts();
  },
  mounted() {
    this.searchRandomProduct();
    this.startUpdatingProducts();
  },
  methods: {
    searchRandomProduct() {
      axios
        .get(`${this.API_URL}/searchProduct/3/0`)
        .then((response) => {
          this.products = response.data;
        })
        .catch((error) => {
          console.error("Error fetching random products:", error);
        });
    },
    startUpdatingProducts() {
      this.updateInterval = setInterval(() => {
        this.searchRandomProduct();
      }, 3000);
    },
    stopUpdatingProducts() {
      clearInterval(this.updateInterval);
    },
    resumeUpdatingProducts() {
      this.startUpdatingProducts();
    },
    handleMouseMove(event, productId) {
      // 處理滑鼠移動事件
    },
    handleMouseEnter(event, productId) {
      // 處理滑鼠進入事件
    },
    handleMouseLeave(event, productId) {
      // 處理滑鼠離開事件
    },
  },
};
</script>

<style scoped>
.card {
  border-radius: 1rem;
  transition-duration: 0.3s;
  background-color: #d9f1e0;
  overflow: hidden;

  &:hover {
    transform: translateY(-6px);
    box-shadow: 0 4px 20px rgba(15, 15, 15, 0.35);
    background-color: #88dccf;
  }
}
</style>
