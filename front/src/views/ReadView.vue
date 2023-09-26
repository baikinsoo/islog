<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "axios";
import router from "@/router";

const props = defineProps({
  postId:{
    type:[Number, String],
    required:true,
  }
})

const post=ref({
  id:0,
  title:"",
  content:"",
})

const moveToEdit=()=>{
  router.push({name:"edit", params: {postId:props.postId}})
}

onMounted(()=>{
  axios.get(`/api/posts/${props.postId}`).then(response=>{
    console.log(response)
    post.value=response.data;
  });
})

const toDelete=() =>{
  axios.delete(`/api/posts/${props.postId}`).then(()=>{
    router.replace({name:"home"})
  });
}

</script>
<template>
  <el-row>
    <el-col>
      <h2 class="title">{{ post.title }}</h2>
      <div class="sub d-flex">
        <div class="category">개발</div>
        <div class="regdate">2023.09.26</div>
      </div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="content">{{post.content}}</div>
    </el-col>
  </el-row>

  <el-row class="mt-3">
    <el-col>
      <div class="d-flex justify-content-end">
        <el-button type="warning" @click="moveToEdit()">수정</el-button>
        <el-button type="danger" @click="toDelete()">삭제</el-button>
      </div>
    </el-col>
  </el-row>
</template>

<style scoped lang="scss">
.title{
    font-size: 1.6rem;
    font-weight: 600;
    color: #383838;
    margin: 0px;
  }
.sub{
    margin-top: 6px;
    font-size: 0.78rem;

  .regdate{
      margin-left: 10px;
      color: #6b6b6b
    }
  }
.content{
    font-size: 0.95rem;
    margin-top: 12px;
    color: #7e7e7e;
    white-space: break-spaces;
  line-height: 1.5;
  }
</style>