<template>
  <div class="btn-container text-end">
    <!-- 檢舉按鈕 -->
    <button
      class="btn btn-warning"
      data-bs-toggle="modal"
      data-bs-target="#reportModal"
    >
      <i class="bi bi-ban"></i> 檢舉
    </button>

    <!-- 模態對話框 -->
    <div
      class="modal fade"
      id="reportModal"
      tabindex="-1"
      aria-labelledby="reportModalLabel"
      aria-hidden="true"
    >
      <div class="modal-dialog">
        <div class="modal-content">
          <!-- 表單 -->
          <form @submit.prevent="submitReport">
            <div class="modal-header">
              <h5 class="modal-title" id="reportModalLabel">檢舉</h5>
              <button
                type="button"
                class="btn-close"
                data-bs-dismiss="modal"
                aria-label="Close"
              ></button>
            </div>
            <div class="modal-body">
              <div class="form-floating mb-3">
                <select
                  v-model="selectedOption"
                  class="form-select"
                  id="reportReason"
                >
                  <option value="武器/軍事用品">武器/軍事用品</option>
                  <option value="涉及誇大易生誤解或醫療效能">
                    涉及誇大易生誤解或醫療效能
                  </option>
                  <option value="菸酒">菸酒</option>
                  <option value="令人感到不適或違反善良風俗">
                    令人感到不適或違反善良風俗
                  </option>
                  <option value="動物或保育動物及其製品">
                    動物或保育動物及其製品
                  </option>
                  <option value="仿冒品">仿冒品</option>
                  <option value="濫用文字誤導搜尋">濫用文字誤導搜尋</option>
                  <option value="其他">其他</option>
                </select>
                <label for="reportReason">選擇檢舉項目</label>
              </div>
              <div class="form-floating mb-3">
                <textarea
                  v-model="reportDescription"
                  class="form-control"
                  id="reportDescription"
                  placeholder="請輸入檢舉原因(10~250字)"
                  maxlength="250"
                ></textarea>
                <label for="reportDescription">檢舉原因(10~250字)</label>
              </div>
            </div>
            <div class="modal-footer">
              <button
                type="button"
                class="btn btn-secondary"
                data-bs-dismiss="modal"
              >
                取消
              </button>
              <button type="submit" class="btn btn-primary">提交</button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 提交成功提示框 -->
    <div
      v-if="showSuccessAlert"
      class="alert alert-success alert-dismissible fade show"
      role="alert"
    >
      提交成功！
      <button
        type="button"
        class="btn-close"
        @click="dismissSuccessAlert"
        aria-label="Close"
      ></button>
    </div>
  </div>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      selectedOption: "",
      reportDescription: "",
      showSuccessAlert: false,
    };
  },
  mounted() {
    const productId = this.$route.params.id;
    this.productId = productId;
  },
  computed: {
    memberId() {
      return useMemberStore().memberId;
    },
    memberName() {
      return useMemberStore().memberName;
    },
  },
  methods: {
    submitReport() {
      if (!this.selectedOption) {
        alert("請選擇檢舉項目");
        return;
      }
      if (!this.reportDescription) {
        alert("請輸入檢舉原因");
        return;
      }
      if (!this.memberId) {
        alert("請先登入才能檢舉");
        return;
      }
      const data = {
        memberId: this.memberId,
        productId: this.productId,
        reportMessage: this.selectedOption,
        otherMessage: this.reportDescription,
        // createTime: new Date().toISOString(), // 使用當前時間
      };
      axios
        .post(`${this.API_URL}/report`, data)
        .then((response) => {
          const modal = document.getElementById("reportModal");
          modal.classList.remove("show");
          modal.style.display = "none";
          document.body.classList.remove("modal-open");
          const modalBackdrop =
            document.getElementsByClassName("modal-backdrop");
          for (let i = 0; i < modalBackdrop.length; i++) {
            document.body.removeChild(modalBackdrop[i]);
          }
          window.alert("提交成功！感謝您的回饋！");
        })
        .catch((error) => {
          alert("檢舉提交失敗");
        });
    },
  },
};
</script>

<style>
/* 可以根據需要添加自定義樣式 */
</style>
