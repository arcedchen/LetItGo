<template>
  <main class="container" style="margin-top: 20px">
    <div class="content-row">
      <div class="col photo-column">
        <div
          :class="state === 'edit' ? 'photo-container' : 'photo-container2'"
          @click="triggerFileUpload"
        >
          <img
            :src="
              previewImage !== 'edit'
                ? previewImage
                : `${API_URL}/member/photo/${id}`
            "
            class="photo w-100"
          />
          <i
            v-if="this.state === 'edit'"
            class="photo-edit-icon fas fa-upload"
          ></i>
          <input
            type="file"
            accept=".jpg, .png"
            class="d-none"
            ref="memberPhoto"
            @change="previewPhoto"
          />
        </div>
      </div>

      <div class="col form-column">
        <div class="row">
          <div class="col-md-8">
            <div class="row mt-3">
              <div class="col-3 text-start">姓名:</div>
              <div class="col">
                <div v-if="this.state === 'normal'" class="text-display">
                  {{ member.memberName }}
                </div>
                <input
                  v-else
                  type="text"
                  v-model="member.memberName"
                  class="form-control"
                />
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">Email:</div>
              <div class="col">
                <div class="text-display">{{ member.memberEmail }}</div>
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">手機號碼:</div>
              <div class="col">
                <div v-if="this.state === 'normal'" class="text-display">
                  {{ member.memberPhone }}
                </div>
                <input
                  v-else
                  type="tel"
                  v-model="member.memberPhone"
                  class="form-control"
                />
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">地址:</div>
              <div class="col">
                <div v-if="this.state === 'normal'" class="text-display">
                  {{ member.memberAddress }}
                </div>
                <input
                  v-else
                  type="text"
                  v-model="member.memberAddress"
                  class="form-control"
                />
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">生日:</div>
              <div class="col">
                <div v-if="this.state === 'normal'" class="text-display">
                  {{ member.birthDate }}
                </div>
                <input
                  v-else
                  type="date"
                  v-model="member.birthDate"
                  class="form-control"
                />
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">年齡:</div>
              <div class="col">
                <div class="text-display">{{ member.memberAge }}</div>
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">性别:</div>
              <div class="col">
                <div v-if="this.state === 'normal'" class="text-display">
                  <span v-if="member.memberGender === 'male'">男生</span>
                  <span v-if="member.memberGender === 'female'">女生</span>
                  <span v-if="member.memberGender === 'other'">其他</span>
                </div>
                <div v-else class="gender-container">
                  <label class="gender-radio text-display">
                    <input
                      type="radio"
                      name="gender"
                      value="male"
                      v-model="member.memberGender"
                    />
                    <span class="gender-label">男性</span>
                  </label>
                  <label class="gender-radio text-display">
                    <input
                      type="radio"
                      name="gender"
                      value="female"
                      v-model="member.memberGender"
                    />
                    <span class="gender-label">女性</span>
                  </label>
                  <label class="gender-radio text-display">
                    <input
                      type="radio"
                      name="gender"
                      value="other"
                      v-model="member.memberGender"
                    />
                    <span class="gender-label">其他</span>
                  </label>
                </div>
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">創建時間:</div>
              <div class="col">
                <div class="text-display">{{ member.createTime }}</div>
              </div>
            </div>
            <div class="row mt-3">
              <div class="col-3 text-start">最後登入:</div>
              <div class="col">
                <div class="text-display">{{ member.lastLoginTime }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="edit-controls">
      <button
        v-if="this.state === 'normal'"
        @click="switchToEditPage"
        class="edit-button bi bi-pencil-fill text-success"
      ></button>
      <button
        v-if="this.state === 'edit'"
        @click="cancelToEdit"
        class="cancel-button bi bi-x-square text-danger"
      ></button>
      <button
        v-if="this.state === 'edit'"
        @click="sendToEdit"
        class="send-button bi bi-check-square text-primary"
      ></button>
    </div>
  </main>
</template>

<script>
import { useMemberStore } from "@/stores/memberStore";
import axios from "axios";

export default {
  data() {
    return {
      member: [],
      state: "normal",
      previewImage: "edit", // Used for displaying the image
      member: {
        birthDate: null,
        memberAge: null,
      },
    };
  },

  mounted() {
    this.searchMember();
  },

  methods: {
    searchMember() {
      axios
        .get(`${this.API_URL}/member/${this.id}`)
        .then((response) => {
          this.member = response.data;
        })
        .catch((error) => {
          console.error("Error fetching member data:", error);
        });
    },
    switchToEditPage() {
      this.state = "edit";
    },
    cancelToEdit() {
      this.state = "normal";
      this.searchMember(); // Reload the original data
    },
    sendToEdit() {
      const fd = new FormData();
      fd.append("member", JSON.stringify(this.member));
      const file = this.$refs.memberPhoto.files[0];
      if (file) {
        fd.append("memberPhoto", file);
      }
      axios
        .post(`${this.API_URL}/member/update`, fd, {
          headers: { "Content-Type": "multipart/form-data" },
        })
        .then(async (rs) => {
          console.log("修改成功");
          const memberStore = useMemberStore();
          memberStore.setMemberName(this.member.memberName);
          sessionStorage.setItem("loggedInMember", JSON.stringify(this.member));
          await this.searchMember();
          this.state = "normal";
          this.previewImage = "edit";
          location.reload();
        })
        .catch((error) => {
          console.log("修改失敗");
        });
    },
    triggerFileUpload() {
      if (this.state === "edit") {
        this.$refs.memberPhoto.click(); // Trigger the hidden file input
      }
    },
    previewPhoto(event) {
      const file = event.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.previewImage = e.target.result; // Set the preview image
        };
        reader.readAsDataURL(file);
      }
    },
    calculateAge(birthDate) {
      if (!birthDate) return null;
      const birthday = new Date(birthDate);
      const today = new Date();
      let age = today.getFullYear() - birthday.getFullYear();
      const m = today.getMonth() - birthday.getMonth();
      if (m < 0 || (m === 0 && today.getDate() - birthday.getDate() > 0)) {
        age--;
      }
      return age;
    },
  },
  watch: {
    "member.birthDate": function (newVal, oldVal) {
      if (newVal !== oldVal) {
        this.member.memberAge = this.calculateAge(newVal);
      }
    },
  },

  computed: {
    id() {
      return useMemberStore().memberId;
    },
  },
};
</script>

<style scoped>
.container {
  width: 90%; /* 將容器寬度設為可見畫面的 95% */
  max-width: 1200px;
  margin: 0 auto;
  padding: 2% 5%; /* 調整上下左右的內邊距 */
  background-color: #ffffff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  position: relative;
  /* 容器必须是相对定位 */
}

.content-row {
  display: flex;
  justify-content: space-between;
  align-items: start;
  transition: all 0.3s ease;
}

.photo-column,
.form-column {
  flex: 1;
  transition: all 0.3s ease-in-out;
}

.photo-column {
  max-width: 40%; /* 設置照片欄的最大寬度為容器寬度的 40% */
  padding-right: 2%; /* 調整照片欄右側的內邊距 */
}

.form-column {
  max-width: 50%; /* 設置表單欄的最大寬度為容器寬度的 50% */
  padding-left: 2%; /* 調整表單欄左側的內邊距 */
  padding-bottom: 2%; /* 調整表單欄底部的內邊距 */
}

.row {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 20px;
}

.col-3.text-start {
  white-space: nowrap;
}

.text-display {
  padding: 10px;
  padding-left: 20px;
  transition: all 0.2s ease-in-out;
  border: 1px solid #ffffff;
  border-radius: 5px;
  text-align: left;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.form-control:focus {
  border-color: #007bff;
  box-shadow: 0 0 8px rgba(0, 123, 255, 0.2);
  transform: scale(1.02);
}

.edit-controls {
  position: absolute;
  top: 10px;
  right: 10px;
}

.edit-button,
.cancel-button,
.send-button {
  margin: 5px;
  border: none;
  background: none;
  cursor: pointer;
  font-size: 30px;
  transition: color 0.3s ease;
}

.edit-button:hover,
.cancel-button:hover,
.send-button:hover {
  color: #007bff;
}

.photo-container {
  position: relative;
  width: 500px;
  height: 500px;
  margin: 50px auto;
  border-radius: 100%;
  overflow: hidden;
  background-color: #fff;
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.2);
  transition: box-shadow 0.3s ease-in-out;
  perspective: 1000px;
  /* Creates a 3D space */
}

.photo-container2 {
  position: relative;
  width: 500px;
  height: 500px;
  margin: 50px auto;
  border-radius: 100%;
  overflow: hidden;
  background-color: #fff;
}

.photo-container:hover {
  transform: scale(1.05);
  /* Slightly scale up on hover */
  box-shadow: 0 0 50px rgba(27, 13, 61, 0.305);
}

.photo {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.3s ease-in-out;
}

.photo-edit-icon {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 30px;
  color: #fff;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.photo-container:hover .photo-edit-icon {
  opacity: 1;
}

.d-none {
  display: none;
}
</style>
