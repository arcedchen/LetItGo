<template>
  <!-- 展示商品頁面 -->
  <main v-if="showProductPage" class="container" style="min-height: 700px">
    <div class="row">
      <div class="col">
        <h1>我的商品</h1>
      </div>
      <div class="col-6 d-flex justify-content-end">
        <div class="input-group" style="max-width: 400px">
          <select class="form-select" id="search" v-model="mode">
            <option value="productName">搜尋商品名稱</option>
          </select>
          <input
            type="search"
            name="search"
            id="search"
            v-model="searchTextTmp"
            class="form-control"
          />
          <button class="btn btn-primary" @click="search">送出</button>
        </div>
      </div>
    </div>
    <!-- 展示商品區塊 -->
    <div>
      <table class="table table-striped table-hover">
        <!-- 表頭 -->
        <thead>
          <tr class="text-center">
            <th scope="col">#</th>
            <th scope="col">展示圖</th>
            <th scope="col">商品名稱</th>
            <th scope="col">商品狀態</th>
            <th scope="col">商品種類</th>
            <!-- <th scope="col">商品描述</th> -->
            <th scope="col">上架時間</th>
            <th scope="col">修改時間</th>
            <th colspan="3" scope="col">操作</th>
          </tr>
        </thead>
        <!-- 商品列表 -->
        <tbody>
          <tr v-for="(product, index) in products" :key="index">
            <th scope="row">{{ index + 1 }}</th>
            <td>
              <img
                :src="`${this.API_URL}/product/photo/${product.productId}`"
                width="100"
                height="100"
              />
            </td>
            <td>{{ product.productName }}</td>
            <td>
              <span v-if="product.productStatus == 1" class="badge bg-success"
                >上架中</span
              >
              <span v-else class="badge bg-danger">下架中</span>
            </td>
            <td>{{ product.productCategoryName }}</td>
            <!-- <td>{{ product.productDescription }}</td> -->
            <td>{{ product.createTime }}</td>
            <td>{{ product.updateTime }}</td>
            <td>
              <!-- 操作按鈕 -->
              <button
                type="button"
                class="btn btn-primary"
                @click="editProduct(product.productId)"
              >
                修改
              </button>
            </td>
            <td>
              <button
                type="button"
                class="btn btn-danger"
                @click="turnOffProduct(product.productId)"
                v-if="product.productStatus == 1"
              >
                下架
              </button>

              <button
                type="button"
                class="btn btn-success"
                @click="turnOnProduct(product.productId)"
                v-else
              >
                上架
              </button>
            </td>
            <td>
              <button
                type="button"
                class="btn btn-warning"
                @click="deleteProduct(product.productId)"
              >
                刪除
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </main>
  <!-- ---------------------------------------------------------------- -->
  <div v-if="showProductPage" class="row">
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
  <!-- ---------------------------------------------------------------- -->
  <!-- 修改商品相關頁面 -->
  <main v-if="showEditPage" class="container">
    <h1>修改商品</h1>
    <hr />

    <div class="mb-3">
      <label for="productName" class="form-label"><b>商品名稱:</b></label>
      <input
        type="text"
        class="form-control"
        v-model="results.productName"
        required
      />
    </div>

    <div class="mb-3" v-if="results.productId">
      <label for="productPhoto" class="form-label"
        ><b>現有商品圖片預覽</b></label
      >
      <img
        :src="`${this.API_URL}/product/photo/${results.productId}`"
        width="150"
        height="150"
      />
    </div>

    <div class="mb-3">
      <label for="productPhoto" class="form-label"
        ><b>更改商品頁面圖片</b></label
      >
      <input
        type="file"
        accept=".jpg,.png"
        id="productPhoto"
        ref="productPhoto"
        class="form-control"
      />
    </div>

    <!-- 產品類別區塊0417新增 -->
    <div class="row">
      <div class="col-md-3">
        <div class="mb-3">
          <label for="select1" class="form-label"
            ><b>第一層類別(如不修改類別可不選)</b></label
          >
          <select
            class="form-select"
            id="select1"
            v-model="firstCategoryId"
            @change="filterSecondChildCategories()"
          >
            <option selected disabled>請選擇</option>
            <option v-for="fc in firstCategories" :value="fc.productCategoryId">
              {{ fc.categoryName }}
            </option>
          </select>
        </div>
      </div>
      <div
        class="col-md-3"
        v-if="
          firstCategoryId && secondCategories && secondCategories.length > 0
        "
      >
        <div class="mb-3">
          <label for="select2" class="form-label"><b>第二層類別</b></label>
          <select
            class="form-select"
            id="select2"
            v-model="secondCategoryId"
            @change="filterThirdChildCategories()"
          >
            <option selected disabled>請選擇</option>
            <option
              v-for="sc in secondCategories"
              :value="sc.productCategoryId"
            >
              {{ sc.categoryName }}
            </option>
          </select>
        </div>
      </div>
      <div
        class="col-md-3"
        v-if="secondCategoryId && thirdCategories && thirdCategories.length > 0"
      >
        <div class="mb-3">
          <label for="select3" class="form-label"><b>第三層類別</b></label>
          <select
            class="form-select"
            id="select3"
            v-model="thirdCategoryId"
            @change="filterFourthChildCategories()"
          >
            <option selected disabled>請選擇</option>
            <option v-for="tc in thirdCategories" :value="tc.productCategoryId">
              {{ tc.categoryName }}
            </option>
          </select>
        </div>
      </div>
      <div
        class="col-md-3"
        v-if="
          thirdCategoryId && fourthCategories && fourthCategories.length > 0
        "
      >
        <div class="mb-3">
          <label for="select4" class="form-label"><b>第四層類別</b></label>
          <select class="form-select" id="select4" v-model="fourthCategoryId">
            <option selected disabled>請選擇</option>
            <option
              v-for="fc in fourthCategories"
              :value="fc.productCategoryId"
            >
              {{ fc.categoryName }}
            </option>
          </select>
        </div>
      </div>
    </div>

    <div class="mb-3">
      <template v-for="(ps, index) in results.productSpecDtos" :key="index">
        <div class="row g-2 mt-2">
          <div class="col">
            <input
              type="text"
              class="form-control"
              v-model="ps.productSpecName"
              placeholder="產品規格名稱"
              required
            />
          </div>
          <div class="col">
            <input
              type="number"
              class="form-control"
              v-model="ps.stockQuantity"
              placeholder="產品規格庫存"
              required
            />
          </div>
          <div class="col">
            <input
              type="number"
              class="form-control"
              v-model="ps.productSellingPrice"
              placeholder="產品規格實價"
              required
            />
          </div>
          <div class="col">
            <input
              type="number"
              class="form-control"
              v-model="ps.productOriginPrice"
              placeholder="產品規格售價"
              required
            />
          </div>
          <div class="col-auto align-self-end">
            <button
              type="button"
              class="btn btn-danger"
              @click="deleteProductSpec(index)"
            >
              刪除
            </button>
          </div>
        </div>
      </template>
      <button
        type="button"
        class="btn btn-primary mt-2"
        @click="addProductSpec"
      >
        新增商品規格
      </button>
    </div>

    <div class="mb-3">
      <label for="productDescription" class="form-label"
        ><b>商品描述:</b></label
      >

      <div style="border: 1px solid #ccc; margin-top: 10px">
        <Toolbar
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
          style="border-bottom: 1px solid #ccc"
        />
        <Editor
          :defaultConfig="editorConfig"
          :mode="mode"
          v-model="results.productDescription"
          style="height: 400px; overflow-y: hidden"
          @onCreated="handleCreated"
          @onChange="handleChange"
          @onDestroyed="handleDestroyed"
          @onFocus="handleFocus"
          @onBlur="handleBlur"
          @customAlert="customAlert"
          @customPaste="customPaste"
        />
      </div>
    </div>

    <div class="mb-3">
      <input
        type="reset"
        class="btn btn-secondary me-2"
        value="取消變更"
        @click="cancelEdit"
      />
      <button type="button" class="btn btn-primary" @click="updateProduct">
        產品變更
      </button>
    </div>
  </main>
</template>

<script>
import "@wangeditor/editor/dist/css/style.css";
import { onBeforeUnmount, ref, shallowRef, onMounted } from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import axios from "axios";

export default {
  components: { Editor, Toolbar },
  setup() {
    // 编辑器实例，必须用 shallowRef
    const editorRef = shallowRef();

    // 内容 HTML
    const valueHtml = ref("<p>hello</p>");

    // 模拟 ajax 异步获取内容
    onMounted(() => {
      setTimeout(() => {
        valueHtml.value = "<p></p>";
      }, 1500);
    });

    const toolbarConfig = {};
    const editorConfig = { placeholder: "請輸入商品描述..." };

    // 组件销毁时，也及时销毁编辑器
    onBeforeUnmount(() => {
      const editor = editorRef.value;
      if (editor == null) return;
      editor.destroy();
    });

    const handleCreated = (editor) => {
      editorRef.value = editor; // 记录 editor 实例，重要！
    };

    const handleChange = (editor) => {
      // console.log("change:", editor.children);
    };
    const handleDestroyed = (editor) => {
      // console.log("destroyed", editor);
    };
    const handleFocus = (editor) => {
      // console.log("focus", editor);
    };
    const handleBlur = (editor) => {
      // console.log("blur", editor);
    };
    const customAlert = (info, type) => {
      alert(`【自定义提示】${type} - ${info}`);
    };
    const customPaste = (editor, event, callback) => {
      // console.log("ClipboardEvent 粘贴事件对象", event);
    };

    return {
      editorRef,
      valueHtml,
      mode: "default", // 或 'simple'
      toolbarConfig,
      editorConfig,
      handleCreated,
      handleChange,
      handleDestroyed,
      handleFocus,
      handleBlur,
      customAlert,
      customPaste,
    };
  },
  data() {
    return {
      mode: "productName",
      searchTextTmp: "",
      searchText: "",
      products: [],
      productStatus: "",
      deleteProductSpecId: [],

      showProductPage: true,
      showEditPage: false,

      results: [
        {
          productId: "",
          productName: "",
          productDescription: "",
          memberId: "",
          productCategoryId: "",
          productSpecDtos: [
            {
              productSpecId: "",
              productSpecName: "",
              stockQuantity: "",
              productOriginPrice: "",
              productSellingPrice: "",
              productId: "",
            },
          ],
        },
      ],
      productCategorys: [],
      firstCategories: [],
      firstCategoryId: "",
      secondCategories: [],
      secondCategoryId: "",
      thirdCategories: [],
      thirdCategoryId: "",
      fourthCategories: [],
      fourthCategoryId: "",

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
    this.searchBySearchText();
    this.searchCategory();
  },
  methods: {
    search() {
      this.searchText = this.searchTextTmp;
      this.searchBySearchText();
    },
    searchBySearchText() {
      if (this.searchText == "") {
        this.searchText = "all";
      }
      axios
        .get(
          `${this.API_URL}/member/searchProduct/${this.searchText}/${this.currentPage}`
        )
        .then((rs) => {
          this.products = rs.data;
          this.currentPage = 0;
          if (rs.data != "") {
            this.totalPage = rs.data[0].totalPage;
          } else {
            this.totalPage = 1;
          }
        });
    },
    editProduct(productId) {
      this.showProductPage = false;
      this.showEditPage = true;
      axios
        .get(`${this.API_URL}/product/${productId}`)
        .then((response) => {
          this.results = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
      axios
        .get(`${this.API_URL}/product/spec/${productId}`)
        .then((response) => {
          this.results.productSpecDtos = response.data;
        })
        .catch((error) => {
          console.error(error);
        });
    },
    cancelEdit() {
      //取消變更產品，改變 showProductPage與showEditPage兩個變數，讓展示頁面顯示，而修改資訊頁面隱藏
      this.showProductPage = true;
      this.showEditPage = false;
    },
    addProductSpec() {
      //檢查現有規格上限（上限為5）
      if (this.results.productSpecDtos.length < 5) {
        // 創建空集合加入規格集合
        const newProductSpec = {
          productSpecName: "",
          stockQuantity: "",
          productSellingPrice: "",
          productOriginPrice: "",
        };
        // 將新的產品規格添加至現有規格集合
        this.results.productSpecDtos.push(newProductSpec);
      } else {
        // 達到上限再按新增，出現跳窗提示
        window.alert("單一產品最多只能有5個規格");
      }
    },
    deleteProduct(productId) {
      axios
        .post(`${this.API_URL}/product/deleteProductStatus/${productId}`)
        .then((response) => {
          console.log("Product delete successfully:");
          window.alert("刪除商品成功");
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error delete product:", error);
        });
    },
    deleteProductSpec(index) {
      // 確認索引在有效範圍內
      if (index >= 0 && index < this.results.productSpecDtos.length) {
        // 獲取要刪除的產品規格
        const deletedSpec = this.results.productSpecDtos[index];
        // 如果該規格的 productSpecId 不為空，則將其添加到 deleteSpec 陣列中
        if (deletedSpec.productSpecId !== "") {
          this.deleteProductSpecId.push(deletedSpec.productSpecId);
          console.log(this.deleteProductSpecId);
        }
        // 刪除指定索引的產品規格
        this.results.productSpecDtos.splice(index, 1);
        // 檢查是否至少還剩一種產品規格
        if (this.results.productSpecDtos.length === 0) {
          // 如果已經沒有產品規格了，彈出提示
          window.alert("至少要有一種規格");
          // 創建空集合加入規格集合
          const newProductSpec = {
            productSpecName: "",
            stockQuantity: "",
            productSellingPrice: "",
            productOriginPrice: "",
          };
          // 將新的產品規格添加至現有規格集合
          this.results.productSpecDtos.push(newProductSpec);
        }
      } else {
        // 索引無效，彈出提示
        window.alert("刪除規格失敗");
      }
    },
    updateProduct() {
      // 創建新的 productSpecs 變數來存儲要複製的資料
      const productSpecs = this.results.productSpecDtos.map((spec) => ({
        productSpecId: spec.productSpecId,
        productSpecName: spec.productSpecName,
        productOriginPrice: spec.productOriginPrice,
        productSellingPrice: spec.productSellingPrice,
        stockQuantity: spec.stockQuantity,
      }));

      if (this.fourthCategoryId) {
        this.results.productCategoryId = this.fourthCategoryId;
      } else if (this.thirdCategoryId) {
        this.results.productCategoryId = this.thirdCategoryId;
      } else if (this.secondCategoryId) {
        this.results.productCategoryId = this.secondCategoryId;
      } else if (this.firstCategoryId) {
        this.results.productCategoryId = this.firstCategoryId;
      }

      const productData = {
        productCategory: this.results.productCategory,
        productDescription: this.results.productDescription,
        productId: this.results.productId,
        productName: this.results.productName,
        productCategoryId: this.results.productCategoryId,
      };

      const deleteProductSpecId = this.deleteProductSpecId;
      //創建表單格式資料
      const fd = new FormData();
      //塞入產品資訊
      fd.append("productData", JSON.stringify(productData));
      //塞入產品規格資訊
      fd.append("productSpecs", JSON.stringify(productSpecs));
      fd.append("deleteProductSpecId", JSON.stringify(deleteProductSpecId));
      // 檢查是否上傳新圖片
      const file = this.$refs.productPhoto.files[0];
      if (file) {
        // 如果有新的文件，则将其添加到 FormData 中
        fd.append("productPhoto", file);
      }
      // 發送 POST 請求到後端
      axios
        .post(`${this.API_URL}/product/updateProduct`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        })
        .then((response) => {
          window.alert("修改成功");
          console.log("修改成功");
          this.showProductPage = true;
          this.showEditPage = false;
          this.$nextTick(() => {
            window.location.reload();
          });
        })
        .catch((error) => {
          console.log("修改失敗");
        });
    },
    turnOffProduct(productId) {
      axios
        .post(`${this.API_URL}/product/updateProductStatus/${productId}`)
        .then((response) => {
          console.log("Product turned off successfully:");
          window.alert("下架商品成功");
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error turning off product:", error);
        });
    },
    turnOnProduct(productId) {
      axios
        .post(`${this.API_URL}/product/updateProductStatus/${productId}`)
        .then((response) => {
          console.log("Product turned on successfully:");
          window.alert("上架商品成功");
          window.location.reload();
        })
        .catch((error) => {
          console.error("Error turning on product:", error);
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
    searchCategory() {
      axios
        .get(`${this.API_URL}/productCategory`)
        .then((response) => {
          this.productCategorys = response.data;
          // console.log(this.productCategorys);
          //過濾出 parentCategory 為 null 的資料(即過濾出第一層類別)
          this.firstCategories = this.productCategorys.filter(
            (productCategory) => productCategory.parentCategory === null
          );
          // console.log(this.firstCategories);
        })
        .catch((error) => {
          console.error(error);
        });
    },
    //此函數為依所選第一層類別篩選第二層類別
    filterSecondChildCategories() {
      this.secondCategories = this.productCategorys.filter(
        (productCategory) =>
          productCategory.parentCategory &&
          productCategory.parentCategory.productCategoryId ===
            this.firstCategoryId
      );
      console.log(this.secondCategories);
    },
    //此函數為依所選第二層類別篩選第三層類別
    filterThirdChildCategories() {
      this.thirdCategories = this.productCategorys.filter(
        (productCategory) =>
          productCategory.parentCategory &&
          productCategory.parentCategory.productCategoryId ===
            this.secondCategoryId
      );
    },
    //此函數為依所選第三層類別篩選第四層類別
    filterFourthChildCategories() {
      this.fourthCategories = this.productCategorys.filter(
        (productCategory) =>
          productCategory.parentCategory &&
          productCategory.parentCategory.productCategoryId ===
            this.thirdCategoryId
      );
    },
  },
  watch: {
    currentPage(newValue, oldValue) {
      if (newValue !== oldValue) {
        axios
          .get(
            `${this.API_URL}/member/searchProduct/${this.searchText}/${this.currentPage}`
          )
          .then((rs) => {
            this.products = rs.data;
            this.totalPage = this.totalPage = rs.data[0].totalPage;
          });
      }
    },
  },
};
</script>

<style></style>
