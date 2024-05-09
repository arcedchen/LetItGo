<template>
  <main class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <h1>不成立訂單</h1>
      </div>
      <div class="col-6 d-flex justify-content-end">
        <div class="input-group" style="max-width: 400px">
          <select class="form-select" id="search" v-model="currentTab">
            <option value="memberOrderId">搜尋訂單ID</option>
            <option value="memberName">搜尋購買人</option>
          </select>
          <input
            v-if="currentTab == 'memberOrderId'"
            type="search"
            name="search"
            id="search"
            v-model="searchTextTmp1"
            class="form-control"
            @input="limitToNumbers"
          />
          <input
            v-if="currentTab != 'memberOrderId'"
            type="search"
            name="search"
            id="search"
            v-model="searchTextTmp2"
            class="form-control"
          />
          <button class="btn btn-primary" @click="search">送出</button>
        </div>
      </div>
    </div>

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
          <td>$ {{ o.totalAmount }}</td>
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
                    <h5 class="modal-title" id="exampleModalLabel">訂單明細</h5>
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
                          <td>${{ mod.unitPrice }}</td>
                          <td>{{ mod.star }}</td>
                          <td>{{ mod.comment }}</td>
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
                </div>
              </div>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </main>
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
  <!-- ---------------------------------------------------------------- -->
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      mode: "16",
      searchText: "all",
      searchTextTmp1: "",
      searchTextTmp2: "",
      currentTab: "memberOrderId",

      orders: [],
      currentPage: 0,
      totalPage: 1,
    };
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
  mounted() {
    this.searchByMember();
  },
  methods: {
    limitToNumbers(event) {
      this.searchTextTmp1 = event.target.value.replace(/\D/g, "");
    },
    search() {
      if (this.currentTab == "memberOrderId" && this.searchTextTmp1 != "") {
        this.mode = "26";
        this.searchText = this.searchTextTmp1;
        this.searchTextTmp1 = "";
      } else if (this.currentTab == "memberName" && this.searchTextTmp2 != "") {
        this.mode = "36";
        this.searchText = this.searchTextTmp2;
        this.searchTextTmp2 = "";
      } else {
        this.mode = "16";
        this.searchText = "all";
      }
      this.searchByMember();
      this.$forceUpdate();
    },
    searchByMember() {
      axios
        .get(
          `${this.API_URL}/order/${this.mode}/${this.searchText}/${this.currentPage}`
        )
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
          .get(
            `${this.API_URL}/order/${this.mode}/${this.searchText}/${this.currentPage}`
          )
          .then((rs) => {
            this.orders = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>
<style></style>
