<template>
  <div id="app">
    <div class="container-fluid">
      <!-- step選單--------------------------------------------------------------------------------- -->
      <section class="container my-5">
        <div class="h2 text-secondary text-center">Let it 購 | 購物車</div>
        <div class="row mt-3">
          <div class="col-lg-3 text">
            <!-- --------------------------------------------------------------------------------- -->
            <div
              class="alert alert-light d-flex flex-column align-items-center alert-rounded"
              v-if="step != 1"
            >
              <small>STEP 1.</small>
              <i class="fas fa-shopping-cart mb-1"></i>
              <p class="h5">購物車清單</p>
            </div>
            <div
              class="alert alert-primary d-flex flex-column align-items-center alert-rounded"
              v-if="step == 1"
            >
              <small>STEP 1.</small>
              <i class="fas fa-shopping-cart mb-1"></i>
              <p class="h5">購物車清單</p>
            </div>
            <!-- --------------------------------------------------------------------------------- -->
          </div>
          <div class="col-lg-3">
            <!-- --------------------------------------------------------------------------------- -->
            <div
              class="alert alert-light d-flex flex-column align-items-center alert-rounded"
              v-if="step != 2"
            >
              <small>STEP 2.</small>
              <i class="fas fa-info mb-1"></i>
              <p class="h5">輸入收貨資料</p>
            </div>
            <div
              class="alert alert-primary d-flex flex-column align-items-center alert-rounded"
              v-if="step == 2"
            >
              <small>STEP 2.</small>
              <i class="fas fa-info mb-1"></i>
              <p class="h5">輸入收貨資料</p>
            </div>
            <!-- --------------------------------------------------------------------------------- -->
          </div>
          <div class="col-lg-3">
            <!-- --------------------------------------------------------------------------------- -->
            <div
              class="alert alert-light d-flex flex-column align-items-center alert-rounded"
              v-if="step != 3"
            >
              <small>STEP 3.</small>
              <i class="fas fa-clipboard-list mb-1"></i>
              <p class="h5">確認訂單並付款</p>
            </div>
            <div
              class="alert alert-primary d-flex flex-column align-items-center alert-rounded"
              v-if="step == 3"
            >
              <small>STEP 3.</small>
              <i class="fas fa-clipboard-list mb-1"></i>
              <p class="h5">確認訂單並付款</p>
            </div>
            <!-- --------------------------------------------------------------------------------- -->
          </div>
          <div class="col-lg-3">
            <!-- --------------------------------------------------------------------------------- -->
            <div
              class="alert alert-light d-flex flex-column align-items-center alert-rounded"
              v-if="step != 4"
            >
              <small>STEP 4.</small>
              <i class="fas fa-check-circle mb-1"></i>
              <p class="h5">完成購買</p>
            </div>
            <div
              class="alert alert-primary d-flex flex-column align-items-center alert-rounded"
              v-if="step == 4"
            >
              <small>STEP 4.</small>
              <i class="fas fa-check-circle mb-1"></i>
              <p class="h5">完成購買</p>
            </div>
            <!-- --------------------------------------------------------------------------------- -->
          </div>
        </div>
      </section>
      <!-- strp1-------------------------------------------------------------------------------- -->
      <div v-if="step == 1">
        <div class="h3 bg-light text-center text-secondary py-3">
          請確認購物車清單
        </div>
        <div id="collapseCart" class="collapse show" data-parent="#cartDetail">
          <div class="table-responsive my-3">
            <table class="table">
              <thead>
                <tr>
                  <th>賣家id</th>
                  <th>商品圖片</th>
                  <th>品名</th>
                  <th>規格</th>
                  <th>單價</th>
                  <th>數量</th>
                  <th>總計</th>
                  <th>刪除</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="p in cartItems" :key="p.productSpecId">
                  <td>{{ p.memberId }}</td>
                  <td>
                    <img
                      :src="getProductPhotoUrl(p.productId)"
                      class="card-img-top"
                      style="width: 100px; height: 100px"
                    />
                  </td>
                  <td>
                    <p class="lead fw-normal mb-2 fs-5">{{ p.productName }}</p>
                  </td>
                  <td>
                    {{ p.productSpecName }}
                  </td>
                  <td>$ {{ p.productSellingPrice }}</td>
                  <td>
                    <div class="input-group" style="width: 150px">
                      <div class="input-group-prepend">
                        <button
                          class="btn btn-outline-secondary rounded-left"
                          type="button"
                          @click="decreaseQuantity(p)"
                        >
                          -
                        </button>
                      </div>
                      <input
                        type="text"
                        class="form-control text-center"
                        v-model="p.quantity"
                        style="width: 50px"
                      />
                      <div class="input-group-append">
                        <button
                          class="btn btn-outline-secondary rounded-right"
                          type="button"
                          @click="increaseQuantity(p)"
                        >
                          +
                        </button>
                      </div>
                      <div class="text-danger">
                        還剩 {{ p.stockQuantity }} 件
                      </div>
                    </div>
                  </td>
                  <td>
                    {{ calculateTotal(p) }}
                  </td>
                  <td>
                    <a href="#!" class="text-danger" @click="removeItem(p)">
                      <i class="fas fa-trash fa-lg"></i>
                    </a>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="mt-2" style="text-align: center">
          <span style="font-weight: bold" class="h4">總計: </span>
          <span style="font-weight: bold" class="h4">{{ totalPrice }}</span>
        </div>
        <!-- <div class="text-success mb-2" style="text-align: center">
          <span style="font-weight: bold">如有折扣碼可於後面輸入~</span>
        </div> -->
        <div class="row mt-3">
          <div class="col">
            <div class="d-flex justify-content-end align-items-center">
              <router-link
                v-if="cartItemCount !== 0"
                to="/"
                class="btn btn-primary btn-lg mx-3"
              >
                繼續購物
              </router-link>
              <button
                v-if="cartItemCount !== 0"
                type="button"
                class="btn btn-primary btn-lg mx-3"
                @click="plusStep"
              >
                下一步
              </button>
              <router-link v-else to="/" class="btn btn-primary btn-lg">
                趕緊購物去
              </router-link>
            </div>
          </div>
        </div>
      </div>
      <!-- step2--------------------------------------------------------------------------------- -->
      <div v-if="step == 2">
        <div class="h3 bg-light text-center text-secondary py-3">
          輸入訂購人資訊
        </div>
        <div class="form-row" style="display: flex; gap: 10px">
          <div class="form-group" style="flex: 0.5">
            <label for="name"
              >訂購人姓名 <span class="text-danger">*</span></label
            >
            <input
              type="text"
              class="form-control"
              id="name"
              placeholder="請輸入姓名"
              v-model="deliveryName"
              required
            />
          </div>
          <div class="form-group" style="flex: 0.5">
            <label for="tel">連絡電話 <span class="text-danger">*</span></label>
            <input
              type="tel"
              class="form-control"
              id="tel"
              placeholder="請輸入電話"
              v-model="deliveryPhone"
              required
            />
          </div>
          <div class="form-group" style="flex: 0.5">
            <label for="address"
              >寄送地址 <span class="text-danger">*</span></label
            >
            <input
              type="text"
              class="form-control"
              id="address"
              placeholder="請輸入地址"
              v-model="deliveryAddress"
              required
            />
          </div>
        </div>
        <br />
        <div class="form-group">
          <label for="shipping"
            >寄送資訊 <span class="text-danger">*</span></label
          >
          <select
            class="form-control"
            id="shipping"
            name="shippingOption"
            v-model="deliveryMethod"
            @change="openModal"
          >
            <option value="" disabled selected>請選擇寄送方式</option>
            <option value="7-ELEVEN">7-ELEVEN $60</option>
            <option value="全家">全家 $60</option>
            <option value="萊爾富">萊爾富 $60</option>
            <option value="OK Mart">OK Mart $60</option>
            <option value="宅配寄送">宅配寄送 $120</option>
          </select>
        </div>
        <div class="modal" id="shippingModal" tabindex="-1" role="dialog">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">寄送方式信息</h5>
              </div>
              <div class="modal-body">
                <div v-if="deliveryMethod === 'SevenEleven'">
                  寄送到 7-ELEVEN，運費為 $60，請填寫寄送地址。
                </div>
                <div v-else-if="deliveryMethod === 'FamilyMart'">
                  寄送到全家，運費為 $60，請填寫寄送地址。
                </div>
                <div v-else-if="deliveryMethod === 'LyleFu'">
                  寄送到萊爾富，運費為 $60，請填寫寄送地址。
                </div>
                <div v-else-if="deliveryMethod === 'OKMart'">
                  寄送到 OK Mart，運費為 $60，請填寫寄送地址。
                </div>
                <div v-else-if="deliveryMethod === 'HomeDelivery'">
                  選擇宅配寄送，運費為 $120，請填寫寄送地址。
                </div>
              </div>
            </div>
          </div>
        </div>
        <br />
        <div class="form-group">
          <label for="message">備註</label>
          <textarea
            class="form-control"
            id="message"
            cols="20"
            rows="5"
            placeholder="管理員代收/電話聯絡時間"
            v-model="deliveryMessage"
          ></textarea>
        </div>
        <div class="row mt-3">
          <div class="col">
            <div class="d-flex justify-content-end align-items-center">
              <button
                v-if="cartItemCount !== 0"
                type="button"
                class="btn btn-primary btn-lg mx-3"
                @click="minusStep"
              >
                上一步
              </button>
              <button
                v-if="cartItemCount !== 0"
                type="button"
                class="btn btn-primary btn-lg mx-3"
                @click="plusStep"
              >
                下一步
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- step3--------------------------------------------------------------------------------- -->
      <div v-if="step == 3">
        <div class="h3 bg-light text-center text-secondary py-3">
          訂單明細確認
        </div>
        <p class="text-center my-4">
          親愛的顧客您好，<br />感謝您訂購我們的優質商品，以下是您的訂購資訊。
        </p>
        <div class="row justify-content-center my-3">
          <div class="col-lg-10">
            <div class="table-responsive">
              <table class="table mb-3 text-center">
                <thead>
                  <th colspan="2">收件人資料</th>
                </thead>
                <tr>
                  <th>姓名</th>
                  <td>{{ deliveryName }}</td>
                </tr>
                <tr>
                  <th>連絡電話</th>
                  <td>{{ deliveryPhone }}</td>
                </tr>
                <tr>
                  <th>寄送地址</th>
                  <td>{{ deliveryAddress }}</td>
                </tr>
                <tr>
                  <th>寄送方式</th>
                  <td>{{ deliveryMethod }}</td>
                </tr>
              </table>
            </div>
          </div>
        </div>
        <div id="collapseCart" class="collapse show" data-parent="#cartDetail">
          <div class="table-responsive my-3">
            <table class="table">
              <thead>
                <tr>
                  <th>賣家id</th>
                  <th>商品圖片</th>
                  <th>品名</th>
                  <th>規格</th>
                  <th>單價</th>
                  <th>數量</th>
                  <th>總計</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="p in cartItems" :key="p.productSpecId">
                  <td>{{ p.memberId }}</td>
                  <td>
                    <img
                      :src="getProductPhotoUrl(p.productId)"
                      class="card-img-top"
                      style="width: 100px; height: 100px"
                    />
                  </td>
                  <td>
                    <p class="lead fw-normal mb-2 fs-5">{{ p.productName }}</p>
                  </td>
                  <td>{{ p.productSpecName }}</td>
                  <td>$ {{ p.productSellingPrice }}</td>
                  <td>{{ p.quantity }}</td>
                  <td>{{ calculateTotal(p) }}</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="mt-2" style="text-align: center">
          <span style="font-weight: bold" class="h5">運費: </span>
          <span style="font-weight: bold" class="h5">{{
            this.deliveryFee
          }}</span>
        </div>
        <div class="mt-2" style="text-align: center">
          <span style="font-weight: bold" class="h4">總計: </span>
          <span style="font-weight: bold" class="h4">{{
            parseInt(totalPrice) + parseInt(this.deliveryFee)
          }}</span>
        </div>
        <div class="mt-3 text-center">
          <div class="h3 bg-light text-secondary py-3">選擇付款方式</div>
          <div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                name="payment"
                id="ecpay"
                value="ecpay"
                v-model="paymentMethod"
              />
              <label class="form-check-label" for="ecpay"> ecpay </label>
            </div>
            <div class="form-check form-check-inline">
              <input
                class="form-check-input"
                type="radio"
                name="payment"
                id="linePay"
                value="LinePay"
                v-model="paymentMethod"
              />
              <label class="form-check-label" for="linePay"> LinePay </label>
            </div>
          </div>
        </div>
        <div class="row mt-3">
          <div class="col">
            <div class="d-flex justify-content-end align-items-center">
              <button
                v-if="cartItemCount !== 0"
                type="button"
                class="btn btn-primary btn-lg mx-3"
                @click="minusStep"
              >
                上一步
              </button>
              <button
                v-if="cartItemCount !== 0"
                type="button"
                class="btn btn-primary btn-lg mx-3"
                @click="handlePaymentAndRemoveItemAndMinusQuantity"
              >
                去付款
              </button>
            </div>
          </div>
        </div>
      </div>
      <!-- step4--------------------------------------------------------------------------------- -->
    </div>
  </div>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      cartItems: [],
      step: 1,

      deliveryFee: 0,
      deliveryName: "",
      deliveryPhone: "",
      deliveryAddress: "",
      deliveryMessage: "",
      deliveryMethod: "",
      paymentMethod: "",
    };
  },
  mounted() {
    this.getCartItems();
  },
  methods: {
    async handlePaymentAndRemoveItemAndMinusQuantity() {
      // 從購物車中移除商品
      for (let i = 0; i < this.cartItems.length; i++) {
        if (this.cartItems[i].quantity <= this.cartItems[i].stockQuantity) {
          await this.removeItem(this.cartItems[i]);
          await this.minusQuantity(this.cartItems[i]);
          await this.addOrder();
        }
      }
    },
    minusQuantity(p) {
      const newQuantity = p.stockQuantity - p.quantity;
      axios
        .post(`${this.API_URL}/product/spec/${newQuantity}/${p.productSpecId}`)
        .then((rs) => {
          console.log(正確更新數量);
        })
        .catch((error) => {
          console.log(沒有正確更新數量);
        });
    },
    plusStep() {
      if (this.step < 4) {
        this.step++;
      }
    },
    minusStep() {
      if (this.step > 1) {
        this.step--;
      }
    },
    getCartItems() {
      axios
        .get(`${this.API_URL}/cart`)
        .then((rs) => {
          this.cartItems = rs.data;
          if (this.cartItems.length != 0) {
            this.getMember();
          }
        })
        .catch((error) => {
          console.log(沒有抓到購物車資料);
        });
    },
    getMember() {
      axios
        .get(`${this.API_URL}/member/${this.id}`)
        .then((rs) => {
          console.log("API-member-memberId");
          this.deliveryName = rs.data.memberName;
          this.deliveryPhone = rs.data.memberPhone;
          this.deliveryAddress = rs.data.memberAddress;
        })
        .catch((error) => {
          console.log(沒有抓到購物車資料);
        });
    },
    calculateTotal(item) {
      return (item.quantity * item.productSellingPrice).toFixed(2);
    },
    increaseQuantity(p) {
      axios
        .post(`${this.API_URL}/product/plus/${p.productSpecId}/1`)
        .then(() => {
          if (p.stockQuantity > p.quantity) {
            p.quantity++;
          }
        })
        .catch((error) => {
          console.log("沒有抓到購物車資料");
        });
    },
    decreaseQuantity(p) {
      if (p.quantity > 1) {
        axios
          .post(`${this.API_URL}/product/minus/${p.productSpecId}/1`)
          .then(() => {
            p.quantity--;
          })
          .catch((error) => {
            console.log("沒有抓到購物車資料");
          });
      }
    },
    async removeItem(p) {
      await axios
        .delete(`${this.API_URL}/product/delete/${p.productSpecId}/1`)
        .then(() => {
          console.log("刪除成功");
        })
        .catch((error) => {
          console.log("沒有抓到購物車資料");
        });
      this.getCartItems();
    },
    getProductPhotoUrl(productId) {
      return `${this.API_URL}/product/photo/${productId}`;
    },
    addOrder() {
      // 構建商品資料
      const memberOrderDetailData = this.cartItems.map((item) => {
        return {
          quantity: item.quantity,
          unitPrice: item.productSellingPrice,
          productName: item.productName,
          productId: item.productId,
          productSpecId: item.productSpecId,
        };
      });
      const totalAmount =
        parseFloat(this.totalPrice) + parseFloat(this.deliveryFee);
      const memberOrderData = {
        totalAmount: totalAmount,
        paymentMethod: this.paymentMethod,
        deliveryName: this.deliveryName,
        deliveryPhone: this.deliveryPhone,
        deliveryAddress: this.deliveryAddress,
        deliveryMessage: this.deliveryMessage,
        deliveryMethod: this.deliveryMethod,
        deliveryFee: this.deliveryFee,
      };
      const fd = new FormData();
      fd.append("memberOrderDto", JSON.stringify(memberOrderData));
      fd.append("memberOrderDetailDto", JSON.stringify(memberOrderDetailData));
      axios
        .post(`${this.API_URL}/addOrder`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        })
        .then((response) => {
          console.log("新增成功");
          this.pay(response.data);
        })
        .catch((error) => {
          console.log("新增失敗");
        });
    },
    pay(UUID) {
      axios
        .post(`${this.API_URL}/pay/${UUID}`)
        .then((response) => {
          console.log("進付款成功");

          if (this.paymentMethod === "LinePay") {
            const paymentUrl = response.data;
            window.location.href = paymentUrl;
          } else if (this.paymentMethod === "ecpay") {
            console.log(response.data);
            // 取得綠界回應的HTML表單
            const form = response.data;
            // 将表單塞入頁面中
            document.body.innerHTML += form;
            // 自動提交表單
            console.log(response.data);
            document.forms[0].submit();
          }
        })
        .catch((error) => {
          this.message = "進付款失敗";
        });
    },
    updateDeliveryFee() {
      // 根據 deliveryMethod 的值更新 deliveryFee
      if (
        this.deliveryMethod === "7-ELEVEN" ||
        this.deliveryMethod === "全家" ||
        this.deliveryMethod === "萊爾富" ||
        this.deliveryMethod === "OK Mart"
      ) {
        this.deliveryFee = 60;
      } else if (this.deliveryMethod === "宅配寄送") {
        this.deliveryFee = 120;
      } else {
        this.deliveryFee = 0;
      }
    },
  },
  computed: {
    cartItemCount() {
      return this.cartItems.length;
    },
    totalPrice() {
      return this.cartItems
        .reduce(
          (total, item) => total + item.quantity * item.productSellingPrice,
          0
        )
        .toFixed(0);
    },
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
  watch: {
    deliveryMethod: {
      handler() {
        this.updateDeliveryFee();
      },
      immediate: true,
    },
  },
};
</script>

<style></style>
