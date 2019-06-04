export default {
  register(detail) {
    return new Promise((resolve, reject) => {
      detail.emailAddress === "gerhard@home.de"
        ? resolve({ result: "success" })
        : reject(new Error("User already exist"));
    });
  }
};
