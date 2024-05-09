<template>
  <div class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <!-- 分頁選項 -->
        <!-- <ul class="nav nav-tabs">
          <li class="nav-item" @click="filterReports('全部')">
            <a class="nav-link" :class="{ active: currentTab === '全部' }"
              >全部</a
            >
          </li>
          <li class="nav-item" @click="filterReports('待處理')">
            <a class="nav-link" :class="{ active: currentTab === '待處理' }"
              >待處理</a
            >
          </li>
          <li class="nav-item" @click="filterReports('已成立')">
            <a class="nav-link" :class="{ active: currentTab === '已成立' }"
              >成立</a
            >
          </li>
          <li class="nav-item" @click="filterReports('不成立')">
            <a class="nav-link" :class="{ active: currentTab === '不成立' }"
              >不成立</a
            >
          </li>
        </ul> -->

        <!-- 報告列表 -->
        <table class="table table-striped table-hover">
          <thead>
            <tr class="text-center">
              <th style="width: 50px">ID</th>
              <th style="width: 200px">創建時間</th>
              <th style="width: 100px">檢舉人 ID</th>
              <th style="width: 100px">產品 ID</th>
              <th style="width: 100px">訂單 ID</th>
              <th style="width: 100px">店家 ID</th>
              <th style="width: 100px">報告訊息</th>
              <th style="width: 250px">其他訊息</th>
              <th style="width: 100px">報告狀態</th>
              <th style="width: 100px" colspan="2">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="report in filteredReports" :key="report.reportId">
              <td>{{ report.reportId }}</td>
              <td>{{ report.createTime }}</td>
              <td>{{ report.memberId }}</td>
              <td>{{ report.productId }}</td>
              <td>{{ report.orderId }}</td>
              <td>{{ report.storeId }}</td>
              <td>{{ report.reportMessage }}</td>
              <td>{{ report.otherMessage }}</td>
              <td>{{ report.reportStatus }}</td>
              <td v-if="report.reportStatus === '待處理'">
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="resolveReportYes(report)"
                >
                  成立
                </button>
              </td>
              <td v-if="report.reportStatus === '待處理'">
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="resolveReportNot(report)"
                >
                  不成立
                </button>
              </td>
            </tr>
          </tbody>
        </table>
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
      reports: [],
      currentTab: "全部",

      currentPage: 0,
      totalPage: 1,
    };
  },
  mounted() {
    this.fetchReports();
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
    filteredReports() {
      if (this.currentTab === "全部") {
        return this.reports;
      } else {
        return this.reports.filter(
          (report) => report.reportStatus === this.currentTab
        );
      }
    },
  },
  methods: {
    fetchReports() {
      axios
        .get(`${this.API_URL}/report/all/${this.currentPage}`)
        .then((rs) => {
          this.reports = rs.data;
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        })
        .catch((error) => {
          console.error("獲取報告列表時發生錯誤:", error);
        });
    },
    filterReports(status) {
      this.currentTab = status;
    },
    //檢舉成立
    resolveReportYes(report) {
      const fd = new FormData();
      fd.append("report", JSON.stringify(report));
      axios
        .post(`${this.API_URL}/report/changeReportTo8`, fd)
        .then((response) => {
          console.log("檢舉成立");
          window.location.reload();
        })
        .catch((error) => {
          console.error("檢舉成立失敗:", error);
        });
    },
    //檢舉不成立(changeReportNot0)
    resolveReportNot(report) {
      const fd = new FormData();
      fd.append("report", JSON.stringify(report));
      axios
        .post(`${this.API_URL}/report/changeReportNot0`, fd)
        .then((response) => {
          console.log("檢舉不成立");
          window.location.reload();
        })
        .catch((error) => {
          console.error("檢舉不成立失敗:", error);
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
          .get(`${this.API_URL}/report/all/${this.currentPage}`)
          .then((rs) => {
            this.reports = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>

<style scoped></style>
