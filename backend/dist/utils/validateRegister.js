"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.validRegister = void 0;
const validRegister = (options) => {
    if (!options.email.includes('@')) {
        return [{
                field: 'email',
                message: "Invalid email",
            }];
    }
    if (options.username.length <= 2) {
        return [{
                field: 'username',
                message: "Length must be greater than 2",
            }];
    }
    if (options.username.includes('@')) {
        return [{
                field: 'username',
                message: "Cannot include an @",
            }];
    }
    if (options.password.length <= 2) {
        return [{
                field: 'password',
                message: "Length must be greater than 2",
            }];
    }
    return null;
};
exports.validRegister = validRegister;
