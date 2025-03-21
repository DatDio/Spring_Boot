package com.example.learn_spring_boot.services.users;

import com.example.learn_spring_boot.Utils.ApiResponse;
import com.example.learn_spring_boot.Utils.PageableObject;
import com.example.learn_spring_boot.dtos.users.CreateUserRequest;
import com.example.learn_spring_boot.dtos.users.SearchUserRequest;
import com.example.learn_spring_boot.dtos.users.UpdateUserRequest;
import com.example.learn_spring_boot.dtos.users.UserDto;
import com.example.learn_spring_boot.entities.Users;
import com.example.learn_spring_boot.mapper.UserMapper;
import com.example.learn_spring_boot.repositories.users.UserRepository;
import com.example.learn_spring_boot.specification.UserSpecification;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Date;
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public ApiResponse<UserDto> createUser(CreateUserRequest request) {
        Users user = userMapper.toEntity(request);
        Users savedUser = userRepository.save(user);
        return ApiResponse.success("User created successfully!", userMapper.toDto(savedUser));
    }

    @Override
    public ApiResponse<UserDto> updateUser(Long id, UpdateUserRequest userDetails) {
        return userRepository.findById(id).map(user -> {
            userMapper.updateUserFromDto(userDetails, user);
            userRepository.save(user);
            return ApiResponse.success("User updated successfully!", userMapper.toDto(user));
        }).orElseGet(() -> ApiResponse.failure("User not found!"));
    }

    @Override
    public ApiResponse<String> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ApiResponse.success("User deleted successfully!", "User ID: " + id);
        }
        return ApiResponse.failure("User not found!");
    }

    @Override
    public ApiResponse<PageableObject<UserDto>> searchUsers(SearchUserRequest request) {
        try {
            Specification<Users> spec = UserSpecification.filterUsers(request);

            String sortBy = StringUtils.hasText(request.getSortBy()) ? request.getSortBy() : "createAt";
            String direction = StringUtils.hasText(request.getDirection()) ? request.getDirection() : "asc";

            Sort sort = direction.equalsIgnoreCase("desc")
                    ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(Math.max(request.getPage() - 1, 0), request.getSize(), sort);

            Page<Users> userPage = userRepository.findAll(spec, pageable);
            List<UserDto> userDtos = userPage.getContent().stream().map(userMapper::toDto).collect(Collectors.toList());

            boolean hasNextPage = userPage.hasNext();
            long totalPages = userPage.getTotalPages();
            int currentPage = userPage.getNumber() + 1; // Page trong Spring bắt đầu từ 0, nên cộng thêm 1

            PageableObject<UserDto> response = new PageableObject<>(userDtos, hasNextPage, totalPages, currentPage);
            return ApiResponse.success("Fetched users successfully!", response);
        } catch (Exception e) {
            return ApiResponse.failure(e.getMessage());
        }
    }


    @Transactional
    @Override
    public ApiResponse<PageableObject<UserDto>> searchUsersNativeQuery(SearchUserRequest request) {
        try {
            StringBuilder sql = new StringBuilder("SELECT * FROM ApplicationUsers WHERE 1=1");
            StringBuilder countSql = new StringBuilder("SELECT COUNT(1) FROM ApplicationUsers WHERE 1=1");
            Map<String, Object> queryParams = new HashMap<>();

            if (StringUtils.hasText(request.getUserName())) {
                sql.append(" AND USERNAME LIKE :userName");
                countSql.append(" AND USERNAME LIKE :userName");
                queryParams.put("userName", "%" + request.getUserName() + "%");
            }
            if (StringUtils.hasText(request.getEmail())) {
                sql.append(" AND EMAIL LIKE :email");
                countSql.append(" AND EMAIL LIKE :email");
                queryParams.put("email", "%" + request.getEmail() + "%");
            }
            if (StringUtils.hasText(request.getPhoneNumber())) {
                sql.append(" AND PHONENUMBER LIKE :phoneNumber");
                countSql.append(" AND PHONENUMBER LIKE :phoneNumber");
                queryParams.put("phoneNumber", "%" + request.getPhoneNumber() + "%");
            }
            if (StringUtils.hasText(request.getGender())) {
                sql.append(" AND GENDER = :gender");
                countSql.append(" AND GENDER = :gender");
                queryParams.put("gender", request.getGender());
            }

            // Sắp xếp dữ liệu (Sort)
            String sortBy = StringUtils.hasText(request.getSortBy()) ? request.getSortBy() : "create_at";
            String direction = "desc".equalsIgnoreCase(request.getDirection()) ? "DESC" : "ASC";
            sql.append(" ORDER BY ").append(sortBy).append(" ").append(direction);

            // Tính tổng số bản ghi
            Query countQuery = entityManager.createNativeQuery(countSql.toString());
            queryParams.forEach(countQuery::setParameter);
            long totalElements = ((Number) countQuery.getSingleResult()).longValue();

            // Phân trang
            int page = Math.max(request.getPage() - 1, 0);
            int size = request.getSize();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            boolean hasNextPage = page + 1 < totalPages;

            Query query = entityManager.createNativeQuery(sql.toString(), Users.class);
            queryParams.forEach(query::setParameter);
            query.setFirstResult(page * size);
            query.setMaxResults(size);

            List<Users> users = query.getResultList();
            List<UserDto> userDtos = users.stream().map(userMapper::toDto).collect(Collectors.toList());

            PageableObject<UserDto> response = new PageableObject<>(userDtos, hasNextPage, totalPages, request.getPage());

            return ApiResponse.success("Fetched users successfully!", response);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failure(e.getMessage());
        }
    }


    @Override
    public ApiResponse<PageableObject<UserDto>> searchUsersProcedure(SearchUserRequest request) {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("search_users");

            // Đăng ký tham số IN
            query.registerStoredProcedureParameter("p_id", Long.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_userName", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_phoneNumber", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_gender", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_dateFrom", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_dateTo", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_createdFrom", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_createdTo", Date.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_sortBy", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_direction", String.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_page", Integer.class, ParameterMode.IN);
            query.registerStoredProcedureParameter("p_size", Integer.class, ParameterMode.IN);

            // Đăng ký REF_CURSOR và tham số OUT
            query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);
            query.registerStoredProcedureParameter("p_totalCount", Integer.class, ParameterMode.OUT);

            // Gán giá trị tham số
            query.setParameter("p_id", request.getId());
            query.setParameter("p_userName", request.getUserName());
            query.setParameter("p_email", request.getEmail());
            query.setParameter("p_phoneNumber", request.getPhoneNumber());
            query.setParameter("p_gender", request.getGender());
            query.setParameter("p_dateFrom", request.getDateOfBirthFrom() != null ? Date.valueOf(request.getDateOfBirthFrom()) : null);
            query.setParameter("p_dateTo", request.getDateOfBirthTo() != null ? Date.valueOf(request.getDateOfBirthTo()) : null);
            query.setParameter("p_createdFrom", request.getCreatedFrom() != null ? Date.valueOf(request.getCreatedFrom()) : null);
            query.setParameter("p_createdTo", request.getCreatedTo() != null ? Date.valueOf(request.getCreatedTo()) : null);
            query.setParameter("p_sortBy", request.getSortBy() != null ? request.getSortBy() : "createAt");
            query.setParameter("p_direction", request.getDirection() != null ? request.getDirection() : "asc");
            query.setParameter("p_page", request.getPage() > 0 ? request.getPage() : 1);
            query.setParameter("p_size", request.getSize() > 0 ? request.getSize() : 10);

            // Thực thi procedure
            query.execute();

            // Lấy tổng số bản ghi
            Integer totalRecords = (Integer) query.getOutputParameterValue("p_totalCount");
            if (totalRecords == null) totalRecords = 0;

            // Lấy danh sách user từ REF_CURSOR
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList(); // Lấy từ cursor

            List<UserDto> userDtos = resultList.stream().map(this::mapToUserDto).collect(Collectors.toList());

            // Tính tổng số trang
            long totalPages = (totalRecords > 0) ? (long) Math.ceil((double) totalRecords / request.getSize()) : 1;
            boolean hasNextPage = request.getPage() < totalPages;

            // Tạo đối tượng phân trang
            PageableObject<UserDto> pageableObject = new PageableObject<>(userDtos, hasNextPage, totalPages, request.getPage());

            return ApiResponse.success("Fetched users successfully!", pageableObject);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failure("Error fetching users: " + e.getMessage());
        }
    }



    @Override
    public List<UserDto> searchAllUsersProcedure() {
        try {
            StoredProcedureQuery query = entityManager.createStoredProcedureQuery("search_users_simple");

            // Nếu dùng Oracle với REF_CURSOR
            query.registerStoredProcedureParameter("p_result", void.class, ParameterMode.REF_CURSOR);

            query.execute();

            // Lấy danh sách kết quả
            List<Object[]> resultList = query.getResultList();

            // Chuyển đổi dữ liệu từ Object[] sang UserDto
            return resultList.stream().map(this::mapToUserDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private UserDto mapToUserDto(Object[] row) {
        UserDto userDto = null;
        try{
             userDto = new UserDto();
            if (row[0] instanceof Number) {
                userDto.setId(((Number) row[0]).longValue()); // Ép kiểu đúng
            } else {
                userDto.setId(null);
            }

            if (row[3] instanceof String) {
                userDto.setEmail((String) row[3]);
            } else {
                userDto.setEmail(null); // hoặc xử lý phù hợp
            }
            userDto.setPassWord((String) row[4]);
            userDto.setPhoneNumber((String) row[5]);
            userDto.setGender((String) row[6]);
            userDto.setUserName((String) row[7]);
            userDto.setDateOfBirth(row[8] != null ?
                    ((Date) row[8]).toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                    : null);
        }catch(Exception e) {

        }
        return userDto;
    }
}