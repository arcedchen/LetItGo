<template>
  <div class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <!-- <ul class="nav nav-tabs justify-content-start">
          <li class="nav-item">
            <button class="nav-link" @click="changeTab('all')">全部交易</button>
          </li>
          <li class="nav-item">
            <button class="nav-link" @click="changeTab('deposit')">
              轉入錢包
            </button>
          </li>
          <li class="nav-item">
            <button class="nav-link" @click="changeTab('applied')">
              已申請撥款
            </button>
          </li>
          <li class="nav-item">
            <button class="nav-link" @click="changeTab('success')">
              撥款成功
            </button>
          </li>
        </ul> -->
        <div v-if="currentTab === 'all'">
          <table class="table table-striped">
            <thead>
              <tr class="text-center">
                <th style="width: 100px">交易編號</th>
                <th style="width: 100px">會員ID</th>
                <th style="width: 300px">更新時間</th>
                <th style="width: 100px">轉入金額</th>
                <th style="width: 100px">錢包金額</th>
                <th style="width: 200px">狀態</th>
                <th style="width: 100px">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="trade in allTrades" :key="trade.tradeId">
                <td>{{ trade.tradeId }}</td>
                <td>{{ trade.memberId }}</td>
                <td>{{ trade.updateTime }}</td>
                <td>{{ trade.change }}</td>
                <td>{{ trade.walletAmount }}</td>
                <td>{{ trade.status }}</td>
                <td>
                  <!-- 如果狀態為"已申請撥款"，顯示撥款按鈕 -->
                  <button
                    v-if="trade.status === '已申請撥款'"
                    @click="finishTrade(trade.tradeId)"
                    class="btn btn-primary"
                  >
                    撥款
                  </button>
                  <button v-else class="btn btn-gray" disabled>無操作</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="currentTab === 'deposit'">
          <!-- 顯示轉入錢包的交易記錄 -->
          <table class="table table-striped">
            <thead>
              <tr>
                <th style="width: 100px">交易編號</th>
                <th style="width: 200px">會員ID</th>
                <th style="width: 300px">更新時間</th>
                <th style="width: 100px">轉入金額</th>
                <th style="width: 100px">錢包金額</th>
                <th style="width: 200px">狀態</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="trade in depositTrades" :key="trade.tradeId">
                <td>{{ trade.tradeId }}</td>
                <td>{{ trade.memberId }}</td>
                <td>{{ trade.updateTime }}</td>
                <td>{{ trade.change }}</td>
                <td>{{ trade.walletAmount }}</td>
                <td>{{ trade.status }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="currentTab === 'applied'">
          <!-- 顯示已申請撥款的交易記錄 -->
          <table class="table table-striped">
            <thead>
              <tr>
                <th style="width: 100px">交易編號</th>
                <th style="width: 200px">會員ID</th>
                <th style="width: 300px">更新時間</th>
                <th style="width: 100px">轉入金額</th>
                <th style="width: 100px">錢包金額</th>
                <th style="width: 200px">狀態</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="trade in appliedTrades" :key="trade.tradeId">
                <td>{{ trade.tradeId }}</td>
                <td>{{ trade.memberId }}</td>
                <td>{{ trade.updateTime }}</td>
                <td>{{ trade.change }}</td>
                <td>{{ trade.walletAmount }}</td>
                <td>{{ trade.status }}</td>
                <td>
                  <!-- 如果狀態為"已申請撥款"，顯示撥款按鈕 -->
                  <button
                    v-if="trade.status === '已申請撥款'"
                    @click="finishTrade(trade.tradeId)"
                    class="btn btn-primary"
                  >
                    撥款
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div v-if="currentTab === 'success'">
          <!-- 顯示撥款成功的交易記錄 -->
          <table class="table table-striped">
            <thead>
              <tr>
                <th style="width: 100px">交易編號</th>
                <th style="width: 200px">會員ID</th>
                <th style="width: 300px">更新時間</th>
                <th style="width: 100px">轉入金額</th>
                <th style="width: 100px">錢包金額</th>
                <th style="width: 200px">狀態</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="trade in successTrades" :key="trade.tradeId">
                <td>{{ trade.tradeId }}</td>
                <td>{{ trade.memberId }}</td>
                <td>{{ trade.updateTime }}</td>
                <td>{{ trade.change }}</td>
                <td>{{ trade.walletAmount }}</td>
                <td>{{ trade.status }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
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
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      allTrades: [],
      depositTrades: [],
      appliedTrades: [],
      successTrades: [],
      currentTab: "all",

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
    this.fetchAllTrades();
  },
  methods: {
    fetchAllTrades() {
      axios
        .get(`${this.API_URL}/trade/all/${this.currentPage}`)
        .then((rs) => {
          this.allTrades = rs.data;
          // this.depositTrades = response.data.filter(
          //   (trade) => trade.status === "轉入錢包"
          // );
          // this.appliedTrades = response.data.filter(
          //   (trade) => trade.status === "已申請撥款"
          // );
          // this.successTrades = response.data.filter(
          //   (trade) => trade.status === "撥款成功"
          // );
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        })
        .catch((error) => {
          console.error("獲取交易記錄失敗:", error);
        });
    },
    finishTrade(tradeId) {
      axios
        .put(`${this.API_URL}/trade/agree`, null, {
          params: {
            tradeId: tradeId,
          },
        })
        .then(() => {
          // 提示撥款成功
          alert("撥款完成");
          // 重新獲取交易記錄
          this.fetchAllTrades();
        })
        .catch((error) => {
          console.error("撥款失敗:", error);
        });
    },
    changeTab(tabName) {
      this.currentTab = tabName;
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
          .get(`${this.API_URL}/trade/all/${this.currentPage}`)
          .then((rs) => {
            this.allTrades = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
