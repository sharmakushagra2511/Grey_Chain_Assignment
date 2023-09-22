import com.assignment.loanmanagement.Application
import com.assignment.loanmanagement.DTO.LoanRequest
import com.assignment.loanmanagement.Models.Customer
import com.assignment.loanmanagement.Models.Lender
import com.assignment.loanmanagement.Models.Loan
import com.assignment.loanmanagement.Repository.LoanRepository
import com.assignment.loanmanagement.Service.CustomerService
import com.assignment.loanmanagement.Service.LenderService
import com.assignment.loanmanagement.Service.LoanService
import com.assignment.loanmanagement.errors.BadRequestException
import com.assignment.loanmanagement.errors.NotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDate
import java.util.Optional

@SpringBootTest
@ContextConfiguration(classes = [Application::class])
class LoanServiceTests {

    private lateinit var loanService: LoanService
    private lateinit var customerService: CustomerService
    private lateinit var lenderService: LenderService
    private lateinit var loanRepository: LoanRepository

    val customer = Customer(
            id = 1,
            name = "customer1",
            email = "customer1@example.com"
    )

    val lender = Lender(
            id = 2,
            name = "lender1",
            email = "lender1@example.com"
    )

    @BeforeEach
    fun setUp() {
        customerService = mock(CustomerService::class.java)
        lenderService = mock(LenderService::class.java)
        loanRepository = mock(LoanRepository::class.java)
        loanService = LoanService(loanRepository, customerService, lenderService)
    }

    @Test
    fun `given valid loan request, when addLoan is called, then loan is added successfully`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now(),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        loanService.addLoan(loanRequest)

        verify(customerService).findById(1)
        verify(lenderService).findById(2)
        verify(loanRepository).save(any(Loan::class.java))
    }

    @Test
    fun `given invalid amount in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = -100.0,
                remainingAmount = 0.0,
                paymentDate = LocalDate.now(),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given remainingAmount is greater than amount in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1500.0,
                paymentDate = LocalDate.now(),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given payment date ahead of current date in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now().plusDays(1),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given due date behind payment date in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now(),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().minusDays(1),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given payment date ahead of due date in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now().plusDays(30),
                interestPerDay = 0.05,
                dueDate = LocalDate.now(),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given negative interest rate in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now(),
                interestPerDay = -0.01,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = 0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given negative penalty percentage in loan request, when addLoan is called, then BadRequestException is thrown`() {
        val loanRequest = LoanRequest(
                customerId = 1,
                lenderId = 2,
                amount = 1000.0,
                remainingAmount = 1000.0,
                paymentDate = LocalDate.now(),
                interestPerDay = 0.05,
                dueDate = LocalDate.now().plusDays(30),
                penaltyPerDay = -0.01,
                cancel = false
        )

        `when`(customerService.findById(1)).thenReturn(Optional.of(customer))
        `when`(lenderService.findById(2)).thenReturn(Optional.of(lender))

        assertThrows<BadRequestException> { loanService.addLoan(loanRequest) }
    }

    @Test
    fun `given no loans found, when getAllLoans is called, then NotFoundException is thrown`() {
        // Arrange
        `when`(loanRepository.findAll()).thenReturn(emptyList())

        // Act & Assert
        assertThrows<NotFoundException> { loanService.getAllLoans() }
    }
}
