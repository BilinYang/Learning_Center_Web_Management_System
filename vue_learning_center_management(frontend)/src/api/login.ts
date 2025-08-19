import type { ResultModel,LoginEmp } from '@/api/model/model'
import request from '@/utils/request'

// login
export const loginApi = (loginEmp:LoginEmp) => request.post<any, ResultModel>(`/login`, loginEmp)
