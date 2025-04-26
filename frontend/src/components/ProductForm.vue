//ProductForm.vue
<template>
  <div>
    <h2>{{ product.id ? "상품 수정" : "상품 등록" }}</h2>
    <form @submit.prevent="handleSubmit">
      <input v-model="product.name" placeholder="상품명" />
      <input
        v-model.number="product.quantity"
        type="number"
        placeholder="수량 (0 이상)"
      />
      <input
        v-model.number="product.price"
        type="number"
        placeholder="가격 (1 이상)"
      />
      <button type="submit">{{ product.id ? "수정" : "등록" }}</button>
    </form>
  </div>
</template>

<script>
import api from "../utils/axios";
import { useToast } from "vue-toastification";

export default {
  props: {
    productToEdit: Object,
  },
  data() {
    return {
      product: {
        id: null,
        name: "",
        quantity: 0,
        price: 0,
      },
      toast: null,
    };
  },
  created() {
    this.toast = useToast();
  },
  watch: {
    productToEdit: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.product = { ...newVal };
        } else {
          this.resetForm();
        }
      },
    },
  },
  methods: {
    async handleSubmit() {
      if (!this.validateInput()) return;

      try {
        if (this.product.id) {
          await api.put(`/products/${this.product.id}`, this.product);
          this.toast.success("상품이 수정되었습니다!");
        } else {
          await api.post("/products", this.product);
          this.toast.success("상품이 등록되었습니다!");
        }

        this.resetForm();
        this.$emit("product-saved");
      } catch (error) {
        this.toast.error(error.response?.data || "저장 실패!");
        console.error("저장 실패:", error);
      }
    },
    validateInput() {
      const { name, quantity, price } = this.product;

      if (!name.trim()) {
        this.toast.error("상품명을 입력해주세요.");
        return false;
      }
      if (!Number.isInteger(quantity) || quantity < 0) {
        this.toast.error("수량은 0 이상의 정수여야 합니다.");
        return false;
      }
      if (!Number.isInteger(price) || price < 1) {
        this.toast.error("가격은 1 이상의 정수여야 합니다.");
        return false;
      }

      return true;
    },
    resetForm() {
      this.product = {
        id: null,
        name: "",
        quantity: 0,
        price: 0,
      };
    },
  },
};
</script>
