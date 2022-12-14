package com.project.numble.application.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.numble.application.common.advice.CommonControllerAdvice;
import com.project.numble.application.common.advice.ControllerAdviceUtils;
import com.project.numble.application.helper.factory.dto.AddAddressRequestFactory;
import com.project.numble.application.helper.factory.dto.AddAnimalsRequestFactory;
import com.project.numble.application.helper.factory.dto.GetAddressResponseFactory;
import com.project.numble.application.helper.factory.dto.GetMyInfoResponseFactory;
import com.project.numble.application.helper.factory.entity.UserFactory;
import com.project.numble.application.user.controller.advice.UserControllerAdvice;
import com.project.numble.application.user.dto.request.AddAddressRequest;
import com.project.numble.application.user.dto.request.AddAnimalsRequest;
import com.project.numble.application.user.dto.response.FindAddressByClientIpResponse;
import com.project.numble.application.user.dto.response.FindAddressByQueryResponse;
import com.project.numble.application.user.dto.response.GetAddressResponse;
import com.project.numble.application.user.dto.response.GetMyInfoResponse;
import com.project.numble.application.user.dto.response.GetUserStaticInfoResponse;
import com.project.numble.application.user.repository.exception.UserNotFoundException;
import com.project.numble.application.user.service.StandardUserService;
import com.project.numble.application.user.service.util.UrlConnectionIOException;
import com.project.numble.core.resolver.UserInfo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@DisplayName("UserController RestDocs ?????????")
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllerDocsTest {

    @Mock
    StandardUserService userService;

    @InjectMocks
    UserController userController;

    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach(RestDocumentationContextProvider restDocumentation) {
        ResourceBundleMessageSource responseHandlerMessageSource = new ResourceBundleMessageSource();
        responseHandlerMessageSource.setBasenames("message/exception_message");
        responseHandlerMessageSource.setDefaultEncoding("UTF-8");

        ResourceBundleMessageSource validatorMessageSource = new ResourceBundleMessageSource();
        validatorMessageSource.setBasenames("message/validation_message");
        validatorMessageSource.setDefaultEncoding("UTF-8");

        ControllerAdviceUtils utils = new ControllerAdviceUtils(responseHandlerMessageSource);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(validatorMessageSource);

        mockMvc = MockMvcBuilders
            .standaloneSetup(userController)
            .setControllerAdvice(
                new UserControllerAdvice(utils),
                new CommonControllerAdvice(utils)
            )
            .setValidator(validator)
            .apply(
                documentationConfiguration(restDocumentation)
                    .operationPreprocessors()
                    .withRequestDefaults(
                        removeHeaders(
                            "Content-Length", "Host"),
                        prettyPrint())
                    .withResponseDefaults(
                        removeHeaders(
                            "Transfer-Encoding", "Date", "Keep-Alive", "Connection", "Content-Type", "Content-Length"),
                        prettyPrint())
            )
            .alwaysDo(print())
            .build();
    }

    @Test
    void searchAddressByIp_??????_?????????_?????????() throws Exception {
        // given
        given(userService.searchAddressByIp())
                .willReturn(
                    new FindAddressByClientIpResponse(
                            "??????????????? ????????? ?????????",
                            "???????????????",
                            "?????????",
                            "?????????")
                );

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address/search/ip"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("search-address-by-ip",
                    responseFields(
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("?????? 1Depth, ?????? ??????"),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????"),
                        fieldWithPath("regionDepth3").type(JsonFieldType.STRING).description("?????? 3Depth, ??? ??????"))
                )
            );
    }

    @Test
    void searchAddressByIp_??????_?????????_?????????() throws Exception {
        // given
        willThrow(UrlConnectionIOException.class).given(userService).searchAddressByIp();

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address/search/ip"));

        // then
        result
            .andExpect(status().isInternalServerError())
            .andDo(
                document("search-address-connection-error")
            );
    }

    @Test
    void searchAddressByQuery_??????_?????????_?????????() throws Exception {
        // given
        given(userService.searchAddressByQuery(anyString()))
            .willReturn(
                new FindAddressByQueryResponse("??????", "??????")
            );

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/address/search/query")
                .queryParam("query", "??????"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("search-address-by-query",
                    requestParameters(
                        parameterWithName("query").description("??????????????? ?????? ?????? ??????(2?????? ??????)")
                    ),
                    responseFields(
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("?????? 1Depth, ?????? ??????").optional(),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????").optional())
                )
            );
    }

    @Test
    void getUserStaticInfo_??????_?????????() throws Exception {
        // given
        GetUserStaticInfoResponse response = GetUserStaticInfoResponse
                .fromUser(UserFactory.createStaticUser(), 1);
        given(userService.getUserStaticInfo(any(UserInfo.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/static-info"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("get-static-user-info",
                    responseFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("profile").type(JsonFieldType.STRING).description("?????????").optional(),
                        fieldWithPath("address").type(JsonFieldType.OBJECT).description("??????").optional(),
                        fieldWithPath("address.addressName").type(JsonFieldType.STRING).description("?????? ?????? ??????").optional(),
                        fieldWithPath("address.regionDepth1").type(JsonFieldType.STRING).description("?????? 1Depth, ?????? ??????").optional(),
                        fieldWithPath("address.regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????").optional(),
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("hasPet").type(JsonFieldType.BOOLEAN).description("??? ?????? ??????"),
                        fieldWithPath("first").type(JsonFieldType.BOOLEAN).description("??? ????????? ??????")
                    )
                )
            );
    }

    @Test
    void getUserStaticInfo_??????_?????????() throws Exception {
        // given
        willThrow(UserNotFoundException.class).given(userService).getUserStaticInfo(any(UserInfo.class));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/static-info"));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("get-static-user-info-failed")
            );
    }

    @Test
    void addAddress_??????_?????????() throws Exception {
        // given
        AddAddressRequest request = AddAddressRequestFactory.newInstance("????????? ?????????", "?????????", "?????????");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("add-address-success",
                    requestFields(
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("?????? 1Depth, ?????? ??????"),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????")
                    )));
    }

    @Test
    void addAddress_addressName_??????_??????_?????????() throws Exception {
        // given
        AddAddressRequest request = AddAddressRequestFactory.newInstance(null, "?????????", "?????????");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("add-address-address-name-failed")
            );
    }

    @Test
    void addAddress_regionDepth1_??????_??????_?????????() throws Exception {
        // given
        AddAddressRequest request = AddAddressRequestFactory.newInstance("????????? ?????????", null, "?????????");

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("add-address-region-1-failed")
            );
    }

    @Test
    void addAddress_regionDepth2_??????_??????_?????????() throws Exception {
        // given
        AddAddressRequest request = AddAddressRequestFactory.newInstance("????????? ?????????", "?????????", null);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isBadRequest())
            .andDo(
                document("add-address-region-2-name-failed"));
    }

    @Test
    void getAddress_??????_?????????() throws Exception {
        // given
        GetAddressResponse response = GetAddressResponseFactory.newInstance("????????? ?????????",
            "?????????", "?????????");
        given(userService.getAddress(any(UserInfo.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/address"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("get-address-success",
                    responseFields(
                        fieldWithPath("addressName").type(JsonFieldType.STRING).description("?????? ?????? ??????").optional(),
                        fieldWithPath("regionDepth1").type(JsonFieldType.STRING).description("?????? 1Depth, ?????? ??????").optional(),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????").optional()
                    )));
    }

    @Test
    void delAddress_??????_?????????() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/users/address"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("del-address-success"));
    }

    @Test
    void addAnimals_??????_?????????() throws Exception {
        // given
        AddAnimalsRequest request = AddAnimalsRequestFactory.newInstance(List.of("?????????", "?????????"));

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.post("/users/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("add-animals-success",
                    requestFields(
                        fieldWithPath("animalTypes")
                            .type(JsonFieldType.ARRAY)
                            .description("?????? ??????(?????????, ?????????, ?????????, ?????????, ?????????, ???, ??????, ??????)")
                            .attributes(key("constraint").value("?????? ?????? ??????")
                    ))));
    }

    @Test
    void withdrawal_??????_?????????() throws Exception {
        // given

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/users/withdrawal"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("withdrawal-user-success"));
    }

    @Test
    void getMyInfo_??????_?????????() throws Exception {
        // given
        GetMyInfoResponse response = GetMyInfoResponseFactory.infoBy("nickname", "profile",
            "?????????", List.of("?????????", "?????????"));
        given(userService.getMyInfo(any(UserInfo.class))).willReturn(response);

        // when
        ResultActions result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/users/my-info"));

        // then
        result
            .andExpect(status().isOk())
            .andDo(
                document("my-info-success",
                    responseFields(
                        fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????????"),
                        fieldWithPath("profile").type(JsonFieldType.STRING).description("?????????").optional(),
                        fieldWithPath("regionDepth2").type(JsonFieldType.STRING).description("?????? 2Depth, ??? ??????").optional(),
                        fieldWithPath("animals")
                            .type(JsonFieldType.ARRAY)
                            .description("?????? ??????(?????????, ?????????, ?????????, ?????????, ?????????, ???, ??????, ??????)")
                    )
                )
            );
    }
}