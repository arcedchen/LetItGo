<template>

  <br>
  <div class="products_box">
    <div class="left" style="background-color: #FFF8D7">
      <div class="imgbox">
        <img :src="`${API_URL}/product/photo/${productId}`" style="width: 450px" height="450px" v-if="productId > 0" />
      </div>
    </div>

    <div class="right" style="background-color: #FFFCEC">
      <div class="row">
        <div class="col">
          <h2 class="mb-3" style="font-family: 'Comic Sans MS'">
            {{ productName }}
          </h2>
        </div>
        <div class="col-auto">
          <ReportButton class="btn float-end"><i class="bi bi-ban"></i></ReportButton>
        </div>
      </div>

      <hr />

      <template v-if="showProductOriginPrice == 0">
        <del class="mr-auto" v-if="minOriginPrice != maxOriginPrice">
          建議售價 NT$ {{ minOriginPrice }} ~ {{ maxOriginPrice }}
        </del>
        <del class="mr-auto" v-if="minOriginPrice == maxOriginPrice">
          建議售價 NT$ {{ minOriginPrice }}
        </del>
        <div class="h3 text-danger">
          價格 NT$
          <strong v-if="minSellingPrice != maxSellingPrice">
            {{ minSellingPrice }} ~ {{ maxSellingPrice }}
          </strong>
          <strong v-if="minSellingPrice == maxSellingPrice">
            {{ minSellingPrice }}
          </strong>
        </div>
      </template>
      <template v-if="showProductOriginPrice != 0">
        <del class="mr-auto">建議售價 NT$ {{ showProductOriginPrice }}</del>
        <div class="h3 text-danger">
          價格 NT$ <strong>{{ showProductSellingPrice }}</strong>
        </div>
      </template>

      <br />
      <div class="mb-3">
        <label class="me-2">規格</label>
        <div class="row">
          <template class="col" v-for="p in productSpecs" :key="p.productSpecId">
            <button v-if="showProductSpecId == p.productSpecId" class="btn me-2 spec-button active"
              @click="getProductButton(p)" :style="{ backgroundColor: '#FFE153', color: 'black' }">
              {{ p.productSpecName }}
            </button>
            <button v-else class="btn btn-outline-secondary me-2 spec-button" @click="getProductButton(p)"
              :style="{ backgroundColor: 'hsl(0, 0%, 95%)', color: 'black' }">
              {{ p.productSpecName }}
            </button>
          </template>
        </div>
      </div>

      <br />

      <div class="mb-3 d-flex align-items-center">
        <label class="me-2">數量</label>
        <button type="button" class="btn btn-secondary btn-sm me-2" @click="decreaseQuantity()">
          -
        </button>
        <input type="number" class="form-control form-control-sm w-25 mx-2" v-model="showStockQuantity" min="1"
          :max="showStockQuantityMax" />
        <button type="button" class="btn btn-secondary btn-sm mx-2" @click="increaseQuantity()">
          +
        </button>
        <span class="text-danger">還剩 {{ showStockQuantityMax }} 件</span>
      </div>
      <br />
      <div class="mb-3">
        <button class="btn btn-primary me-2" @click="insertToCart()">
          <i class="bi bi-cart-plus"></i> 加入購物車
        </button>
        <button class="btn btn-danger me-2" @click="goToCart()">
          <i class="bi bi-bag-check"></i> 直接購買
        </button>
        <span style="color: red">{{ message }}</span>
      </div>
    </div>
  </div>
  <div class="products_box">
    <div class="right">
      <table class="table">
        <thead>
          <tr>
            <th scope="col">訂單id</th>
            <th scope="col">會員名稱</th>
            <th scope="col">購買規格</th>
            <th scope="col">給分數</th>
            <th scope="col">評論</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(comment, index) in comments" :key="index">
            <th scope="row">{{ comment.memberOrderId }}</th>
            <td>{{ comment.memberName }}</td>
            <td>{{ comment.productSpecName }}</td>
            <td v-if="comment && comment.star != null">{{ comment.star }}星</td>
            <td v-if="comment && comment.star == null">尚未評分</td>

            <td v-if="comment && comment.comment != null">
              {{ comment.comment }}
            </td>
            <td v-if="comment && comment.comment == null">尚未評論</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="right">
      <div class="info_box">
        <strong class="title" style="font-size: 24px">商品介紹</strong>
        <hr />
        <h5 class="text" v-html="productDescription"></h5>
      </div>
    </div>
  </div>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import ReportButton from "@/components/ReportButton.vue";
import axios from "axios";

export default {
  data() {
    return {
      message: "",
      productId: 0,
      productName: "",
      productDescription: "",
      memberId: 0,
      productSpecs: [],
      carMemberId: 0,
      countCartItems: 0,

      minOriginPrice: 0,
      maxOriginPrice: 0,
      minSellingPrice: 0,
      maxSellingPrice: 0,

      showProductSpecId: 0,
      showProductOriginPrice: 0,
      showProductSellingPrice: 0,
      showStockQuantityMax: 0,
      showStockQuantity: 1,

      comments: [
        {
          memberOrderId: 0,
          memberId: 0,
          memberName: "",
          productSpecName: "",
          star: 0,
          comment: "",
        },
      ],
    };
  },
  mounted() {
    const productId = this.$route.params.id;
    this.productId = productId;
    this.getProduct();
    this.getProductSpec();
    this.getProductSpecPrice();
    if (this.id != 0) {
      this.getCartMemberIdAndCount();
    }
    this.getProductComment();
  },
  methods: {
    getProduct() {
      axios.get(`${this.API_URL}/product/${this.productId}`).then((rs) => {
        this.productId = rs.data.productId;
        this.productName = rs.data.productName;
        this.productDescription = rs.data.productDescription;
        this.memberId = rs.data.memberId;
      });
    },
    getProductSpec() {
      axios.get(`${this.API_URL}/product/spec/${this.productId}`).then((rs) => {
        this.productSpecs = rs.data;
      });
    },
    getProductSpecPrice() {
      axios
        .get(`${this.API_URL}/product/spec/price/${this.productId}`)
        .then((rs) => {
          this.minOriginPrice = rs.data.minOriginPrice;
          this.maxOriginPrice = rs.data.maxOriginPrice;
          this.minSellingPrice = rs.data.minSellingPrice;
          this.maxSellingPrice = rs.data.maxSellingPrice;
        });
    },
    getProductButton(p) {
      this.showProductSpecId = p.productSpecId;
      this.showProductOriginPrice = p.productOriginPrice;
      this.showProductSellingPrice = p.productSellingPrice;
      this.showStockQuantityMax = p.stockQuantity;
      this.showStockQuantity = 1;
    },
    increaseQuantity() {
      if (this.showStockQuantity < this.showStockQuantityMax) {
        this.showStockQuantity++;
      }
    },
    decreaseQuantity() {
      if (this.showStockQuantity > 1) {
        this.showStockQuantity--;
      }
    },
    insertToCart() {
      if (this.isLoggedIn != true) {
        this.message = "請先登入會員";
      } else if (this.id == this.memberId) {
        this.message = "不能購買自己的商品";
      } else if (this.showProductSpecId == 0) {
        this.message = "請選擇商品規格";
      } else if (this.memberId == this.carMemberId || this.carMemberId == 0) {
        axios
          .get(
            `${this.API_URL}/product/create/${this.showProductSpecId}/${this.showStockQuantity}`
          )
          .then((rs) => {
            this.message = "加入購物車成功";
            this.getCartMemberIdAndCount();
          })
          .catch((error) => {
            this.message = "加入購物車失敗";
          });
      } else {
        this.message = "有其他賣家的商品請先清空購物車";
      }
    },
    goToCart() {
      if (this.isLoggedIn != true) {
        this.message = "請先登入會員";
      } else if (this.id == this.memberId) {
        this.message = "不能購買自己的商品";
      } else if (this.showProductSpecId == 0) {
        this.message = "請選擇商品規格";
      } else if (this.memberId == this.carMemberId || this.carMemberId == 0) {
        axios
          .get(
            `${this.API_URL}/product/create/${this.showProductSpecId}/${this.showStockQuantity}`
          )
          .then((rs) => {
            this.message = "加入購物車成功";
            this.$router.push("/cart");
          })
          .catch((error) => {
            this.message = "加入購物車失敗";
          });
      } else {
        this.message = "有其他賣家的商品請先清空購物車";
      }
    },
    getCartMemberIdAndCount() {
      axios
        .get(`${this.API_URL}/cart`)
        .then((rs) => {
          if (rs.data.length != 0) {
            this.carMemberId = rs.data[0].memberId;
          }
          this.countCartItems = rs.data.length;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getProductComment() {
      axios
        .get(`${this.API_URL}/product/memberCommemt/${this.productId}`)
        .then((rs) => {
          this.comments = rs.data;
          console.log(this.comments);
        });
    },
  },
  components: {
    ReportButton,
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

<style>
.breadcrumb-item a {
  text-decoration: none;
}

.products_info {
  max-width: 1080px;
  width: 100%;
  margin: 0 auto;
  background-position: center center;
  background-size: cover;
}

.products_box {
  width: 100%;
  display: flex;
}

.left {
  flex: 1;
  padding: 20px;
  margin: 5px;
  border: 1px solid #eee;
}

.right {
  flex: 1;
  padding: 20px;
  margin: 5px;
  border: 1px solid #0a0a0a;
}

.imgbox {
  display: flex;
  justify-content: center;
  padding: 20px;

  img {
    max-width: 100%;
    max-height: 100%;
  }
}

.right {
  .product_title {
    font-size: 24px;
    margin: 10px 0;
    letter-spacing: 3px;
  }
}

.spec-button {
  width: 100px;
  border-radius: 8px;
  font-weight: bold;
  margin-bottom: 10px;

}
</style>
