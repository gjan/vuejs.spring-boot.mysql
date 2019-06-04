export default {
  authenticate(detail) {
    return new Promise((resolve, reject) => {
      (detail.username === "gerhard" ||
        detail.username === "gerhard@home.de") &&
      detail.password === "MySuperSecretPwd!"
        ? resolve({ result: "success" })
        : reject(new Error("Invalid credentials"));
    });
  }
};
