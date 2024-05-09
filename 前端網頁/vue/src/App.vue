<template>
  <div id="app">
    <Loading v-if="loadingState.isLoading"></Loading>
    <div v-if="buyerpage">
      <BuyerHeader></BuyerHeader>
      <BuyerFooter></BuyerFooter>
    </div>
    <div v-if="sellerpage">
      <SellerHeader></SellerHeader>
      <SellerSiderbar></SellerSiderbar>
    </div>
    <div v-if="managerpage">
      <ManagerHeader></ManagerHeader>
      <ManagerSiderbar></ManagerSiderbar>
    </div>
  </div>
</template>

<script>
import BuyerHeader from "@/components/BuyerHeader.vue";
import BuyerFooter from "@/components/BuyerFooter.vue";
import SellerSiderbar from "@/components/SellerSidebar.vue";
import SellerHeader from "@/components/SellerHeader.vue";
import ManagerSiderbar from "@/components/ManagerSidebar.vue";
import ManagerHeader from "@/components/ManagerHeader.vue";
import Loading from "@/components/Loading.vue";
import { loadingState } from "@/stores/LoadingState.js";

export default {
  components: {
    BuyerHeader,
    BuyerFooter,
    SellerHeader,
    SellerSiderbar,
    ManagerHeader,
    ManagerSiderbar,
  },
  computed: {
    buyerpage() {
      return this.$route.meta.buyerpage || false;
    },
    sellerpage() {
      return this.$route.meta.sellerpage || false;
    },
    managerpage() {
      return this.$route.meta.managerpage || false;
    },
  },
  data() {
    return {
      loadingState: loadingState,
    };
  },
};
</script>

<style></style>
