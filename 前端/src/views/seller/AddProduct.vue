<template>
  <main class="container">
    <h1>新增商品</h1>
    <hr />
    <div class="mb-3">
      <label for="productName" class="form-label"><b>商品名稱:</b></label>
      <input
        type="text"
        id="productName"
        class="form-control"
        placeholder="請輸入商品名稱"
        name="productName"
        required
        v-model="productName"
      />
    </div>

    <div class="mb-3">
      <label for="productPhoto" class="form-label"><b>商品頁面圖片:</b></label>
      <input
        type="file"
        accept=".jpg,.png"
        id="productPhoto"
        class="form-control"
        name="productPhoto"
        ref="productPhoto"
      />
    </div>

    <div class="row">
      <div class="col-md-3">
        <div class="mb-3">
          <label for="select1" class="form-label"><b>第一層類別</b></label>
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
      <label for="productSpec" class="form-label"><b>商品規格:</b></label>
      <div v-for="(ps, index) in productSpec" :key="index" class="row g-2 mt-2">
        <div class="col">
          <input
            type="text"
            class="form-control"
            placeholder="請輸入商品規格"
            name="productSpecName"
            required
            v-model="productSpec[index].productSpecName"
          />
        </div>
        <div class="col">
          <input
            type="number"
            class="form-control"
            placeholder="請輸入商品庫存"
            name="stockQuantity"
            required
            v-model="productSpec[index].stockQuantity"
          />
        </div>
        <div class="col">
          <input
            type="number"
            class="form-control"
            placeholder="請輸入商品售價"
            name="productSellingPrice"
            required
            v-model="productSpec[index].productSellingPrice"
          />
        </div>
        <div class="col">
          <input
            type="number"
            class="form-control"
            placeholder="請輸入商品建議售價"
            name="productOriginPrice"
            required
            v-model="productSpec[index].productOriginPrice"
          />
        </div>
        <div class="col-auto">
          <button
            type="button"
            class="btn btn-danger"
            @click="deleteProductSpec(index)"
          >
            刪除
          </button>
        </div>
      </div>
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
          v-model="valueHtml"
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

      <!-- 持久化至資料庫預覽 -->
      <!-- <div style="margin-top: 10px">
      <textarea v-model="valueHtml" readonly style="width: 100%; height: 200px; outline: none"></textarea>
    </div> -->
    </div>

    <div class="mb-3">
      <input
        type="reset"
        class="btn btn-secondary me-2"
        value="清除"
        @click="cancelToAddProduct"
      />
      <button type="button" class="btn btn-primary" @click="addProduct">
        送出
      </button>
    </div>
    <div class="text-danger text-center">
      <h2>{{ message }}</h2>
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
      productName: "",
      productCategoryId: "",
      productCategoryName: "",
      memberId: "",
      productDescription: "",
      productSpec: [
        {
          productSpecName: "",
          stockQuantity: "",
          productOriginPrice: "",
          productSellingPrice: "",
        },
      ],
      message: "",

      //接收產品類別資料陣列
      productCategorys: [],

      //接收第一層類別資料陣列
      firstCategories: [],
      //接收使用者選擇第一層類別資料(會塞入第一層類別的類別id，用以篩選後續類別)
      firstCategoryId: "",
      //接收第二層類別資料陣列
      secondCategories: [],
      //接收第二層類別資料(會塞入第二層類別的類別id，用以篩選後續類別)
      secondCategoryId: "",
      //接收第三層類別資料陣列
      thirdCategories: [],
      //接收第三層類別資料(會塞入第三層類別的類別id，用以篩選後續類別)
      thirdCategoryId: "",
      //接收第四層類別資料陣列
      fourthCategories: [],
      //接收第四層類別資料(會塞入第四層類別的類別id，用以篩選後續類別)
      fourthCategoryId: "",
    };
  },

  mounted() {
    this.searchCategory();
  },

  methods: {
    addProduct() {
      if (this.fourthCategoryId) {
        this.productCategoryId = this.fourthCategoryId;
      } else if (this.thirdCategoryId) {
        this.productCategoryId = this.thirdCategoryId;
      } else if (this.secondCategoryId) {
        this.productCategoryId = this.secondCategoryId;
      } else if (this.firstCategoryId) {
        this.productCategoryId = this.firstCategoryId;
      }

      const productData = {
        productName: this.productName,
        memberId: this.memberId,
        productDescription: this.valueHtml,
        productCategoryId: this.productCategoryId,
      };

      const productSpec = this.productSpec;

      const fd = new FormData();
      fd.append("productDto", JSON.stringify(productData));
      fd.append("productSpecDto", JSON.stringify(productSpec));
      fd.append("productPhoto", this.$refs.productPhoto.files[0]);

      // 發送 POST 請求到後端
      axios
        .post(`${this.API_URL}/addProduct`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        })
        .then((response) => {
          this.message = "新增成功";
          window.location.reload();
        })
        .catch((error) => {
          this.message = "新增失敗";
        });
    },

    addProductSpec() {
      //檢查現有規格上限（上限為5）
      if (this.productSpec.length < 5) {
        // 創建空集合加入規格集合
        const newProductSpec = {
          productSpecName: "",
          stockQuantity: "",
          productSellingPrice: "",
          productOriginPrice: "",
        };
        // 將新的產品規格添加至現有規格集合
        this.productSpec.push(newProductSpec);
      } else {
        // 達到上限再按新增，出現跳窗提示
        window.alert("單一產品最多只能有5個規格");
      }
    },

    // 刪除商品規格
    deleteProductSpec(index) {
      // 確認索引在有效範圍內
      if (index >= 0 && index < this.productSpec.length) {
        // 刪除指定索引的產品規格
        this.productSpec.splice(index, 1);
      } else {
        // 如果索引超出範圍，可以提示用戶或執行其他邏輯
        console.warn("索引超出範圍");
      }
    },

    cancelToAddProduct() {
      window.location.reload();
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
};
</script>

<style></style>
