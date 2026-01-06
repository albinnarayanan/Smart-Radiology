const InputField = ({
  label,
  id,
  type = "text",
  errors,
  register,
  required,
  message,
  className,
  min,
  value,
  placeholder,
  rows = 4,
  accept, // new prop for file types
}) => {
  const commonClasses = `${className ? className : ""} px-2 py-2 border outline-none 
    bg-transparent rounded-md text-slate-800
    ${errors?.[id]?.message ? "border-red-500" : "border-slate-700"}`;

  return (
    <div className="flex flex-col gap-1 w-full">
      {label && (
        <label
          htmlFor={id}
          className={`${className ? className : ""} font-semibold text-sm text-slate-800 `}
        >
          {label}
        </label>
      )}

      {type === "textarea" ? (
        <textarea
          id={id}
          placeholder={placeholder}
          rows={rows}
          className={`${commonClasses} w-11/12`}
          {...register(id, {
            required: { value: required, message },
            minLength: min
              ? { value: min, message: `Minimum ${min} characters is required` }
              : undefined,
          })}
          onInput={(e) => {
            e.target.style.height = "auto";
            e.target.style.height = e.target.scrollHeight + "px";
          }}
        />
      ) : type === "file" ? (
        <input
          type="file"
          id={id}
          accept={accept || "image/*"} // default to images
          className={`${commonClasses} w-full`}
          {...register(id, {
            required: { value: required, message },
          })}
        />
      ) : (
        <input
          type={type}
          id={id}
          placeholder={placeholder}
          className={`${commonClasses} w-full`}
          {...register(id, {
            required: { value: required, message },
            minLength: min
              ? { value: min, message: `Minimum ${min} characters is required` }
              : undefined,
          })}
        />
      )}

      {errors?.[id]?.message && (
        <p className="text-red-600 text-sm font-semibold">
          {errors[id]?.message}
        </p>
      )}
    </div>
  )
}

export default InputField
