import { mount, createLocalVue } from "@vue/test-utils";
import VueRouter from "vue-router";
import Vuelidate from "vuelidate";
import registrationService from "@/services/registration";
import RegisterPage from "@/views/RegisterPage";

// Adding Vue Router to the test so that
// we can access vm.$router
const localVue = createLocalVue();
localVue.use(VueRouter);
localVue.use(Vuelidate);
const router = new VueRouter();

// Mock dependency registratioService
jest.mock("@/services/registration");

describe("RegisterPage.vue", () => {
  let wrapper;
  let fieldUsername;
  let fieldEmailAddress;
  let fieldPassword;
  let buttonSubmit;

  let registerSpy;

  beforeEach(() => {
    wrapper = mount(RegisterPage, {
      localVue,
      router
    });
    fieldUsername = wrapper.find("#username");
    fieldEmailAddress = wrapper.find("#emailAddress");
    fieldPassword = wrapper.find("#password");
    buttonSubmit = wrapper.find("form button[type = 'submit']");
    registerSpy = jest.spyOn(registrationService, "register");
  });

  afterEach(() => {
    registerSpy.mockReset();
    registerSpy.mockRestore();
  });

  afterAll(() => {
    jest.restoreAllMocks();
  });

  it("should render correct content", () => {
    expect(wrapper.find(".logo").attributes().src).toEqual(
      "/static/images/logo.png"
    );
    expect(wrapper.find(".tagline").text()).toEqual(
      "Open source task management tool"
    );
    expect(fieldUsername.element.value).toEqual("");
    expect(fieldEmailAddress.element.value).toEqual("");
    expect(fieldPassword.element.value).toEqual("");
    expect(buttonSubmit.text()).toEqual("Create account");
  });

  // Test des VueModels
  it("should contain data model with initial values", () => {
    expect(wrapper.vm.form.username).toEqual("");
    expect(wrapper.vm.form.emailAddress).toEqual("");
    expect(wrapper.vm.form.password).toEqual("");
  });

  it("should have form inputs bound to data model", () => {
    const username = "gerhard";
    const emailAddress = "gerhard@home.de";
    const password = "MySuperSecretPwd";

    wrapper.vm.form.username = username;
    wrapper.vm.form.emailAddress = emailAddress;
    wrapper.vm.form.password = password;

    expect(fieldUsername.element.value).toEqual(username);
    expect(fieldEmailAddress.element.value).toEqual(emailAddress);
    expect(fieldPassword.element.value).toEqual(password);
  });

  it("should have form submit event handler `submitForm`", () => {
    const stub = jest.fn();
    wrapper.setMethods({ submitForm: stub });
    buttonSubmit.trigger("submit");
    expect(stub).toBeCalled();
  });

  it("should register when it is a new user", async () => {
    expect.assertions(2);

    const stub = jest.fn();
    const username = "gerhard";
    const emailAddress = "gerhard@home.de";
    const password = "MySuperSecretPwd";

    wrapper.vm.$router.push = stub;

    wrapper.vm.form.username = username;
    wrapper.vm.form.emailAddress = emailAddress;
    wrapper.vm.form.password = password;
    wrapper.vm.submitForm();
    expect(registerSpy).toBeCalled();
    await wrapper.vm.$nextTick();
    expect(stub).toHaveBeenCalledWith({ name: "LoginPage" });
  });

  it("should fail if it is not a new user", async () => {
    expect.assertions(3);
    // In the mock only "gerhard@home.de" is a new user
    wrapper.vm.form.username = "uschi";
    wrapper.vm.form.emailAddress = "uschi@already-exists.com";
    wrapper.vm.form.password = "AnInsecurePwd";
    expect(wrapper.find(".failed").isVisible()).toBe(false);
    wrapper.vm.submitForm();
    expect(registerSpy).toBeCalled();
    await wrapper.vm.$nextTick();
    expect(wrapper.find(".failed").isVisible()).toBe(false);
  });

  it("should fail it the email address is invalid", () => {
    const spy = jest.spyOn(registrationService, "register");
    wrapper.vm.form.emailAddress = "bad-email-address";
    wrapper.vm.submitForm();
    expect(spy).not.toHaveBeenCalled();
    spy.mockReset();
    spy.mockRestore();
  });

  it("should fail when the username is invalid", () => {
    wrapper.vm.form.username = "a";
    wrapper.vm.form.emailAddress = "good-mailaddres@home.de";
    wrapper.vm.form.password = "good-password";
    wrapper.vm.submitForm();
    expect(registerSpy).not.toHaveBeenCalled();
  });

  it("should fail when the password is invalid", () => {
    wrapper.vm.form.username = "goodusername";
    wrapper.vm.form.emailAddress = "good-mailaddres@home.de";
    wrapper.vm.form.password = "bad!";
    wrapper.vm.submitForm();
    expect(registerSpy).not.toHaveBeenCalled();
  });
});
