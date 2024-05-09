<template>
  <div
    class
    style="
      margin-top: 8px;
      background: #d8c3a6;
      border-radius: 10px;
      padding: 10px;
      box-shadow: 10px 10px 10px rgba(0, 128, 0, 0.5);
      border: 5px dashed black;
    "
  >
    <div class="container mt-3 pt-3" style="min-height: 680px">
      <div class="row">
        <div class="col-md-6">
          <div class="card">
            <div class="card-body">
              <h2 class="card-title">{{ memberName }} 的錢包</h2>
              <p v-if="walletAmount !== null" class="card-text">
                NT$ {{ walletAmount }} 元
              </p>
              <p v-else class="card-text">您的錢包尚未開通</p>

              <button
                v-if="walletAmount > 0"
                @click="showWithdrawForm = true"
                class="btn btn-danger"
              >
                提領
              </button>
            </div>
          </div>
          <div v-if="showWithdrawForm" class="card mt-3">
            <div class="card-body">
              <h2 class="card-title">提領</h2>
              <br />
              <form @submit.prevent="withdraw">
                <div class="form-group">
                  <label for="withdrawAmount">提領金額</label>
                  <input
                    type="number"
                    class="form-control"
                    id="withdrawAmount"
                    v-model="withdrawAmount"
                    :max="walletAmount"
                  />
                </div>
                <br />
                <button type="submit" class="btn btn-primary">確認提領</button>
              </form>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <h2>交易記錄</h2>
          <table class="table">
            <thead>
              <tr>
                <th>交易ID</th>
                <th>會員ID</th>
                <th>更新時間</th>
                <th>變化</th>
                <th>錢包金額</th>
                <th>狀態</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="trade in trades" :key="trade.tradeId">
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
    <!-- ---------------------------------------------------------------- -->
    <div class="row">
      <div class="col"></div>
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
  </div>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      walletAmount: null,
      showWithdrawForm: false,
      withdrawAmount: 0,
      trades: [],

      currentPage: 0,
      totalPage: 0,
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
    memberId() {
      return useMemberStore().memberId;
    },
    memberName() {
      return useMemberStore().memberName;
    },
  },
  mounted() {
    this.fetchLatestTrade();
    this.fetchTrades();
  },
  methods: {
    fetchLatestTrade() {
      axios
        .get(`${this.API_URL}/trade/latest/${this.memberId}`)
        .then((response) => {
          this.walletAmount = response.data.walletAmount;
        })
        .catch((error) => {
          console.error("獲取最新交易失敗:", error);
        });
    },
    fetchTrades() {
      axios
        .get(`${this.API_URL}/trade/${this.memberId}/${this.currentPage}`)
        .then((rs) => {
          this.trades = rs.data;
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
    withdraw() {
      axios
        .post(`${this.API_URL}/trade/createTrade`, null, {
          params: {
            memberId: this.memberId,
            change: this.withdrawAmount * -1,
          },
        })
        .then((response) => {
          console.log("提領成功:", response.data);
          this.showWithdrawForm = false;
          this.fetchLatestTrade();
          this.showSuccessToast();
          this.fetchTrades();
        })
        .catch((error) => {
          console.error("提領失敗:", error);
        });
    },
    showSuccessToast() {
      alert("已申請撥款，款項預計7天內入帳！");
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
          .get(`${this.API_URL}/trade/${this.memberId}/${this.currentPage}`)
          .then((rs) => {
            this.trades = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>

<style scoped>
.container {
  width: 100%;
  min-height: 700px;
  margin: 0 auto;
}

.card {
  margin-bottom: 20px;
  background-color: rgba(255, 255, 255, 0.3);
}

.table {
  margin-top: 20px;
}

.table th,
.table td {
  background-color: rgba(255, 255, 255, 0.3);
  width: 200px;
}
</style>
