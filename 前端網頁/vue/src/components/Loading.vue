<template>
  <div class="loading-overlay">
    <img
      src="../assets/penguin.gif"
      alt="Loading Animation"
      class="loading-gif"
    />
  </div>
</template>

<script>
export default {
  name: "Loading",
};
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-gif {
  width: 200px;
  height: 200px;
}
</style>
