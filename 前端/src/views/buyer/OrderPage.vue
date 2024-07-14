<template>
  <div class="container" style="height: 680px">
    <h2 class="mt-3">買家訂單</h2>
    <br />
    <ul class="nav nav-tabs">
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'all' }"
          @click="changeTab('all')"
          >全部</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'Unpaid' }"
          @click="changeTab('Unpaid')"
          >待付款</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'ToShip' }"
          @click="changeTab('ToShip')"
          >待出貨</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'Shipping' }"
          @click="changeTab('Shipping')"
          >運送中</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'Completed' }"
          @click="changeTab('Completed')"
          >已完成</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'Cancelled' }"
          @click="changeTab('Cancelled')"
          >不成立</a
        >
      </li>
      <li class="nav-item">
        <a
          class="nav-link"
          :class="{ active: currentTab === 'Return' }"
          @click="changeTab('Return')"
          >退貨退款</a
        >
      </li>
    </ul>
    <!-- ---------------------------------------------------------------- -->
    <div>
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th scope="col">訂單ID</th>
            <th scope="col">訂購日期</th>
            <th scope="col">購買人</th>
            <th scope="col">總金額</th>
            <th scope="col">付款方式</th>
            <th scope="col">訂單狀態</th>
            <th scope="col">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="o in orders" :key="o.memberOrderId">
            <td>{{ o.memberOrderId }}</td>
            <td>{{ o.orderDate }}</td>
            <td>{{ o.memberName }}</td>
            <td>{{ o.totalAmount }}</td>
            <td>{{ o.paymentMethod }}</td>
            <td>{{ o.orderStatus }}</td>
            <td>
              <!-- Button trigger modal -->
              <button
                type="button"
                :data-bs-target="'#exampleModal-' + o.memberOrderId"
                data-bs-toggle="modal"
                class="btn btn-primary btn-sm"
              >
                看明細
              </button>
              <!-- Modal -->
              <div
                class="modal fade"
                :id="'exampleModal-' + o.memberOrderId"
                tabindex="-1"
                aria-labelledby="exampleModalLabel"
                aria-hidden="true"
              >
                <div class="modal-dialog modal-xl">
                  <div class="modal-content">
                    <div class="modal-header">
                      <h5 class="modal-title" id="exampleModalLabel">
                        訂單明細
                      </h5>
                      <button
                        type="button"
                        class="btn-close"
                        data-bs-dismiss="modal"
                        aria-label="Close"
                      ></button>
                    </div>
                    <div class="modal-body">
                      <table class="table table-striped table-hover">
                        <thead>
                          <tr>
                            <th scope="col">配送方式</th>
                            <th scope="col">收貨姓名</th>
                            <th scope="col">收貨電話</th>
                            <th scope="col">收貨地址</th>
                            <th scope="col">收貨備註</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr>
                            <td>{{ o.deliveryMethod }}</td>
                            <td>{{ o.deliveryName }}</td>
                            <td>{{ o.deliveryPhone }}</td>
                            <td>{{ o.deliveryAddress }}</td>
                            <td>{{ o.deliveryMessage }}</td>
                          </tr>
                        </tbody>
                      </table>
                      <table class="table table-striped table-hover">
                        <thead>
                          <tr>
                            <th scope="col">訂單ID</th>
                            <th scope="col">商品ID</th>
                            <th scope="col">商品名稱</th>
                            <th scope="col">規格ID</th>
                            <th scope="col">規格名稱</th>
                            <th scope="col">數量</th>
                            <th scope="col">單價</th>
                            <th scope="col">訂單評分</th>
                            <th scope="col">訂單評價</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr
                            v-for="mod in o.memberOrderDetails"
                            :key="mod.orderDetailId"
                          >
                            <td>{{ mod.memberOrderId }}</td>
                            <td>{{ mod.productId }}</td>
                            <td>{{ mod.productName }}</td>
                            <td>{{ mod.productSpecId }}</td>
                            <td>{{ mod.productSpecName }}</td>
                            <td>{{ mod.quantity }}</td>
                            <td>{{ mod.unitPrice }}</td>
                            <td v-if="o.orderStatus == '已完成'">
                              <select
                                id="rating"
                                class="form-select"
                                style="width: auto"
                                v-model="mod.star"
                              >
                                <option value="5">5 / 5</option>
                                <option value="4">4 / 5</option>
                                <option value="3">3 / 5</option>
                                <option value="2">2 / 5</option>
                                <option value="1">1 / 5</option>
                              </select>
                            </td>
                            <td v-else>{{ mod.star }}</td>
                            <td v-if="o.orderStatus == '已完成'">
                              <input
                                type="text form-control-sm"
                                v-model="mod.comment"
                              />
                            </td>
                            <td v-else>{{ mod.comment }}</td>
                          </tr>
                        </tbody>
                        <tbody>
                          <tr></tr>
                          <tr>
                            <td colspan="9" class="text-end">
                              <strong>運費: </strong>{{ o.deliveryFee }}
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                    <!-- ----------------------------------------------------------------- -->
                    <div class="modal-footer" v-if="o.orderStatus === '待付款'">
                      <div class="row g-2 align-items-center">
                        <div class="col col-auto">
                          <button
                            type="button"
                            class="btn btn-danger"
                            data-bs-dismiss="modal"
                            @click="cancel(o.orderStatus, o.memberOrderId)"
                          >
                            取消訂單
                          </button>
                        </div>
                        <div class="col">
                          <button
                            type="button"
                            class="btn btn-success"
                            @click="pay(o)"
                          >
                            去付款
                          </button>
                        </div>
                      </div>
                    </div>
                    <!-- ----------------------------------------------------------------- -->
                    <div
                      class="modal-footer"
                      v-if="
                        o.orderStatus === '待出貨' || o.orderStatus === '運送中'
                      "
                    >
                      <div
                        class="row g-2 align-items-center"
                        v-if="o.orderStatus === '待出貨'"
                      >
                        <div class="col col-auto">
                          <button
                            type="button"
                            class="btn btn-danger"
                            data-bs-dismiss="modal"
                            @click="cancel(o.orderStatus, o.memberOrderId)"
                          >
                            取消訂單
                          </button>
                        </div>
                      </div>
                      <div
                        class="row g-2 align-items-center"
                        v-if="o.orderStatus === '運送中'"
                      >
                        <div class="col col-auto">
                          <button
                            type="button"
                            class="btn btn-danger"
                            data-bs-dismiss="modal"
                            @click="cancel(o.orderStatus, o.memberOrderId)"
                          >
                            取消訂單
                          </button>
                          <button
                            type="button"
                            class="btn btn-success ms-2"
                            data-bs-dismiss="modal"
                            @click="complete(o.orderStatus, o.memberOrderId)"
                          >
                            確認收貨
                          </button>
                        </div>
                      </div>
                    </div>
                    <!-- ----------------------------------------------------------------- -->
                    <div class="modal-footer" v-if="o.orderStatus === '已完成'">
                      <div class="row g-2 align-items-center">
                        <div class="col">
                          <button
                            type="button"
                            class="btn btn-success"
                            data-bs-dismiss="modal"
                            @click="writeComment(o)"
                          >
                            填寫評價
                          </button>
                        </div>
                      </div>
                    </div>
                    <!-- ----------------------------------------------------------------- -->
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
  <!-- ---------------------------------------------------------------- -->
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
          <button class="page-link" v-if="p == '...'">
            {{ p }}
          </button>
          <button class="page-link" v-if="p != '...'">
            {{ p + 1 }}
          </button>
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
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      mode: "1",
      searchText: "",
      currentTab: "all",

      orders: [],
      currentPage: 0,
      totalPage: 0,
    };
  },
  computed: {
    memberId() {
      return useMemberStore().memberId;
    },
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
  mounted() {
    this.getBuyerOrders();
  },
  methods: {
    getBuyerOrders() {
      axios
        .get(`${this.API_URL}/order/${this.mode}/all/${this.currentPage}`)
        .then((rs) => {
          this.orders = rs.data;
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        })
        .catch((error) => {
          console.error(error);
        });
    },
    changeTab(tabName) {
      this.currentTab = tabName;
    },

    pay(o) {
      axios
        .post(`${this.API_URL}/pay/${o.paymentMethodId}`)
        .then((response) => {
          console.log("進付款成功");
          if (o.paymentMethod === "LinePay") {
            const paymentUrl = response.data;
            window.location.href = paymentUrl;
          } else if (o.paymentMethod === "ecpay") {
            const form = response.data;
            document.body.innerHTML += form;
            document.forms[0].submit();
          }
        })
        .catch((error) => {
          this.message = "進付款失敗";
        });
    },
    writeComment(order) {
      const comments = order.memberOrderDetails.map((mod) => ({
        orderDetailId: mod.orderDetailId,
        star: mod.star,
        comment: mod.comment,
      }));

      console.log(comments);
      const fd = new FormData();
      fd.append("comments", JSON.stringify(comments));

      axios
        .post(`${this.API_URL}/order/updateOrderComment`, fd)
        .then((response) => {
          console.log("評論已成功提交");
        })
        .catch((error) => {
          console.error("評論提交失敗:", error);
        });
    },
    complete(orderStatus, memberOrderId) {
      axios
        .post(
          `${this.API_URL}/order/updateOrder/${orderStatus}Complete/${memberOrderId}`
        )
        .then(() => {
          console.log("步驟已完成");
          this.currentTab = "Completed";
        })
        .catch((error) => {
          console.error("步驟失敗:", error);
        });
    },

    cancel(orderStatus, memberOrderId) {
      axios
        .post(
          `${this.API_URL}/order/updateOrder/${orderStatus}Cancel/${memberOrderId}`
        )
        .then(() => {
          console.log("步驟已完成");
          if (orderStatus == "待付款") {
            this.currentTab = "Cancelled";
          } else if (orderStatus == "待出貨" || orderStatus == "運送中") {
            this.currentTab = "Return";
          }
        })
        .catch((error) => {
          console.error("步驟失敗:", error);
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
  },
  watch: {
    currentPage(newValue, oldValue) {
      if (newValue !== oldValue) {
        axios
          .get(`${this.API_URL}/order/${this.mode}/all/${this.currentPage}`)
          .then((rs) => {
            this.orders = rs.data;
            this.totalPage = this.totalPage = rs.data[0].totalPage;
          })
          .catch((error) => {
            console.error(error);
          });
      }
    },
    currentTab(newTab, oldTab) {
      if (this.currentTab === "Unpaid") {
        this.mode = "2";
        this.getBuyerOrders();
      } else if (this.currentTab === "ToShip") {
        this.mode = "3";
        this.getBuyerOrders();
      } else if (this.currentTab === "Shipping") {
        this.mode = "4";
        this.getBuyerOrders();
      } else if (this.currentTab === "Completed") {
        this.mode = "5";
        this.getBuyerOrders();
      } else if (this.currentTab === "Cancelled") {
        this.mode = "6";
        this.getBuyerOrders();
      } else if (this.currentTab === "Return") {
        this.mode = "7";
        this.getBuyerOrders();
      } else {
        this.mode = "1";
        this.getBuyerOrders();
      }
    },
  },
};
</script>

<style scoped>
.container {
  width: 100%;
  height: 700px;
  margin: 0 auto;
}
</style>
