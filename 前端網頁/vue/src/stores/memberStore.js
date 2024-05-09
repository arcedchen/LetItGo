import { defineStore } from "pinia";
export const useMemberStore = defineStore("memberStore", {
  state() {
    return {
      memberId: "",
      memberName: "",
      isLoggedIn: false,
    };
  },
  actions: {
    loginSuccess(member) {
      this.memberId = member.memberId;
      this.memberName = member.memberName;
      this.isLoggedIn = true;
    },
    logout() {
      this.memberId = "";
      this.memberName = "";
      this.isLoggedIn = false;
    },
    setMemberName(memberName){
      this.memberName = memberName;
    }
  },
  getters: {},
});
