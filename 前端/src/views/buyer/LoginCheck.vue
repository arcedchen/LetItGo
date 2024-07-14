<template>
  <div></div>
</template>
<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  created() {},
  mounted() {
    axios
      .get(`${this.OA2_URL}/current-user`)
      .then((rs) => {
        if (rs.data.memberId != null) {
          const memberStore = useMemberStore();
          memberStore.loginSuccess(rs.data);
          sessionStorage.setItem("loggedInMember", JSON.stringify(rs.data));
          this.$router.push("/");
        }
      })
      .catch((error) => {
        console.error("獲取用戶信息失敗", error);
      });
  },
  methods: {},
};
</script>
<style></style>
