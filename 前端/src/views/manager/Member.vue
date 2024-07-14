<template>
  <div class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <table class="table table-striped table-hover">
          <!-- 表頭 -->
          <thead>
            <tr class="text-center">
              <th scope="col">會員id</th>
              <th scope="col">帳號狀態</th>
              <th scope="col">會員圖片</th>
              <th scope="col">會員名稱</th>
              <th scope="col">會員信箱</th>
              <th scope="col">會員電話</th>
              <th scope="col">會員創建時間</th>
              <th scope="col">會員最後登入時間</th>
              <th colspan="3" scope="col">操作</th>
            </tr>
          </thead>
          <!-- 商品列表 -->
          <tbody>
            <tr v-for="(member, index) in members" :key="member.memberId">
              <th scope="row">{{ member.memberId }}</th>
              <td>
                <span v-if="member.accountStatus == 1" class="badge bg-success"
                  >啟用中</span
                >
                <span v-else class="badge bg-danger">停權中</span>
              </td>
              <td>
                <img
                  :src="`${this.API_URL}/member/photo/${member.memberId}`"
                  width="100"
                  height="100"
                />
              </td>
              <td>{{ member.memberName }}</td>

              <td>{{ member.memberEmail }}</td>
              <td>{{ member.memberPhone }}</td>
              <td>{{ member.createTime }}</td>
              <td>{{ member.lastLoginTime }}</td>
              <td>
                <button
                  type="button"
                  class="btn btn-danger"
                  @click="turnOffMember(member.memberId)"
                  v-if="member.accountStatus == 1"
                >
                  將帳號停權
                </button>

                <button
                  type="button"
                  class="btn btn-success"
                  @click="turnOnMember(member.memberId)"
                  v-else
                >
                  將帳號啟用
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
      members: [],

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
    this.searchAllMembers();
  },
  methods: {
    searchAllMembers() {
      axios
        .get(`${this.API_URL}/member/all/${this.currentPage}`)
        .then((rs) => {
          this.members = rs.data;
          console.log(this.members);
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    turnOffMember(memberId) {
      axios
        .post(`${this.API_URL}/member/updateMemberStatus/${memberId}`)
        .then((response) => {
          console.log("Member turned off successfully:");
          window.alert("停權成功");
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error turning off product:", error);
        });
    },
    turnOnMember(memberId) {
      axios
        .post(`${this.API_URL}/member/updateMemberStatus/${memberId}`)
        .then((response) => {
          console.log("Member turned on successfully:");
          window.alert("啟用成功");
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error turning off product:", error);
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
          .get(`${this.API_URL}/member/all/${this.currentPage}`)
          .then((rs) => {
            this.members = rs.data;
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>

<style></style>
