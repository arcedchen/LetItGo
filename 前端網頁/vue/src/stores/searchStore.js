import { defineStore } from "pinia";
export const useSearchStore = defineStore("searchStore", {
  state() {
    return {
      searchText: "",
      productOrSeller: "",
    };
  },
  actions: {
    search(searchText, productOrSeller) {
      this.searchText = searchText;
      this.productOrSeller = productOrSeller;
    },
  },
});
