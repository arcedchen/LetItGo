<template>
  <div class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <table class="table table-striped table-hover">
          <thead>
            <tr class="text-center">
              <th scope="col" class="col-1">#</th>
              <th scope="col" class="col-1">分類名子</th>
              <th scope="col" class="col-1">父類別</th>
              <th scope="col" class="col-3">規格描述</th>
              <th scope="col" class="col-1">排序</th>
              <th scope="col" class="col-1">是否啟用</th>
              <th scope="col" class="col-1" colspan="2">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td></td>
              <td>
                <input
                  type="text"
                  class="form-control"
                  v-model="categoryName"
                />
              </td>
              <td>
                <select
                  class="form-select"
                  aria-label="Default select example"
                  v-model="parentId"
                >
                  <option value=""></option>
                  <option
                    v-for="c in categories"
                    :key="c.productCategoryId"
                    :value="c.productCategoryId"
                  >
                    {{ c.categoryName }}
                  </option>
                </select>
              </td>
              <td>
                <input
                  type="text"
                  class="form-control"
                  v-model="categoryDescription"
                />
              </td>
              <td>
                <input type="number" class="form-control" v-model="sortOrder" />
              </td>
              <td>{{ categoryStatus }}</td>
              <td colspan="2">
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="addProductCategory"
                >
                  新增
                </button>
              </td>
            </tr>
            <tr v-for="(c, index) in categories" :key="c.productCategoryId">
              <td>{{ c.productCategoryId }}</td>
              <td v-if="!c.editMode">{{ c.categoryName }}</td>
              <td v-else><input type="text" v-model="c.categoryName" /></td>
              <td>{{ c.parentCategoryName }}</td>
              <td v-if="!c.editMode">{{ c.categoryDescription }}</td>
              <td v-else>
                <input type="text" v-model="c.categoryDescription" />
              </td>
              <td v-if="!c.editMode">{{ c.sortOrder }}</td>
              <td v-else><input type="text" v-model="c.sortOrder" /></td>
              <td>{{ c.categoryStatus }}</td>
              <td>
                <button
                  type="button"
                  class="btn btn-primary"
                  @click="toggleEditMode(index)"
                >
                  {{ c.editMode ? "保存" : "編輯" }}
                </button>
              </td>
              <td>
                <button
                  type="button"
                  class="btn btn-danger"
                  @click="deleteProductCategory(c)"
                >
                  刪除
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
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
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      categories: [],
      categoryName: "",
      parentId: null,
      categoryDescription: "",
      categoryStatus: "啟用",
      sortOrder: "",

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
    this.getProductCategory();
  },
  methods: {
    toggleEditMode(index) {
      if (this.categories[index].editMode) {
        // 如果編輯模式為 true，表示用戶點擊了保存按鈕，這時候應該將資料傳送至後端
        const editedCategory = {
          productCategoryId: this.categories[index].productCategoryId,
          categoryName: this.categories[index].categoryName,
          categoryDescription: this.categories[index].categoryDescription,
          categoryStatus: this.categories[index].categoryStatus,
          sortOrder: this.categories[index].sortOrder,
        };
        const fd = new FormData();
        fd.append("editedCategory", JSON.stringify(editedCategory));
        axios
          .post(`${this.API_URL}/updateProductCategory`, fd)
          .then((response) => {
            // 在成功接收後端響應後，可以執行一些後續操作，比如提示用戶保存成功
            console.log("保存成功");
            console.log(response.data);
            this.categories[index].editMode = false;
            this.getProductCategory();
          })
          .catch((error) => {
            // 在發生錯誤時，可以提示用戶保存失敗或進行其他處理
            console.error("保存失敗", error);
          });
      } else {
        // 如果編輯模式為 false，表示用戶點擊了編輯按鈕，這時候只需要切換編輯模式即可
        this.categories[index].editMode = true;
      }
    },
    deleteProductCategory(c) {
      const fd = new FormData();
      fd.append("productCategoryId", c.productCategoryId);
      axios
        .post(`${this.API_URL}/deleteProductCategory`, fd)
        .then((response) => {
          console.log("刪除成功");
          this.getProductCategory();
          if (!response.data) {
            window.alert("刪除失敗，請檢查是否有子類別");
          }
        })
        .catch((error) => {
          console.error("刪除失敗", error);
        });
    },
    getProductCategory() {
      axios
        .get(`${this.API_URL}/productCategory/${this.currentPage}`)
        .then((rs) => {
          this.categories = rs.data.map((category) => ({
            ...category,
            editMode: false,
          }));
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        })
        .catch((error) => {
          console.log("登出失敗", error);
        });
    },
    addProductCategory() {
      const parentId = this.parentId ? this.parentId : null;
      const productCategoryData = {
        categoryName: this.categoryName,
        categoryDescription: this.categoryDescription,
        categoryStatus: this.categoryStatus,
        sortOrder: this.sortOrder,
      };
      const fd = new FormData();
      fd.append("productCategory", JSON.stringify(productCategoryData));
      fd.append("parentId", JSON.stringify(parentId));
      axios
        .post(`${this.API_URL}/addProductCategory`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        })
        .then((rs) => {
          this.message = "新增成功";
          location.reload();
        })
        .catch((error) => {
          this.message = "新增失敗";
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
          .get(`${this.API_URL}/productCategory/${this.currentPage}`)
          .then((rs) => {
            this.categories = rs.data.map((category) => ({
              ...category,
              editMode: false,
            }));
            this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>
<style>
input[type="text"] {
  width: 100%; /* 使用百分比設置寬度 */
  box-sizing: border-box; /* 確保寬度包括內邊距和邊框 */
}
</style>
